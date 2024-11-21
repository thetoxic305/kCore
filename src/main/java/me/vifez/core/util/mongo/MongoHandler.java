package me.vifez.core.util.mongo;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.model.ReplaceOptions;

import java.util.Collections;

public final class MongoHandler {

    public static final ReplaceOptions REPLACE_OPTIONS = new ReplaceOptions().upsert(true);

    private static com.mongodb.client.MongoClient syncClient;
    private static com.mongodb.reactivestreams.client.MongoClient asyncClient;

    private MongoHandler() {}

    public static void init(ServerAddress address, boolean authenticate, String authenticationDatabase, String username, String password) {
        MongoClientSettings.Builder settings = MongoClientSettings.builder()
                .applyToClusterSettings(builder -> builder.hosts(Collections.singletonList(address)));

        if (authenticate) {
            settings.credential(MongoCredential.createCredential(username, authenticationDatabase, password.toCharArray()));
        }

        syncClient = com.mongodb.client.MongoClients.create(settings.build());
        asyncClient = com.mongodb.reactivestreams.client.MongoClients.create(settings.build());
    }

    public static void close() {
        syncClient.close();
        asyncClient.close();
    }

    public static Collection getCollection(String database, String collection) {
        return new Collection(syncClient.getDatabase(database).getCollection(collection), asyncClient.getDatabase(database).getCollection(collection));
    }

    public static com.mongodb.client.MongoClient getSyncClient() {
        return syncClient;
    }

    public static com.mongodb.reactivestreams.client.MongoClient getAsyncClient() {
        return asyncClient;
    }

}