package me.vifez.core.util.mongo;

import org.bson.Document;

public class Collection {

    private com.mongodb.client.MongoCollection<Document> sync;
    private com.mongodb.reactivestreams.client.MongoCollection<Document> async;

    public Collection(com.mongodb.client.MongoCollection<Document> sync, com.mongodb.reactivestreams.client.MongoCollection<Document> async) {
        this.sync = sync;
        this.async = async;
    }

    public com.mongodb.client.MongoCollection<Document> sync() {
        return sync;
    }

    public com.mongodb.reactivestreams.client.MongoCollection<Document> async() {
        return async;
    }

}