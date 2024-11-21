package me.vifez.core.util.redis;

import com.google.gson.JsonObject;
import me.vifez.core.kCore;
import me.vifez.core.kCoreConstant;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class RedisHandler {

    private static JedisPool pool;

    private static Map<String, Consumer<JsonObject>> connections = new HashMap<>();

    private static boolean authenticate;
    private static String password;

    private RedisHandler() {}

    public static void init(String host, int port, boolean authenticate, String password) {
        pool = new JedisPool(host, port);

        new Thread(() -> runCommand(jedis -> jedis.subscribe(new JedisPubSub() {

            @Override
            public void onMessage(String channel, String message) {

                if (channel.equals("connection")) {
                    JsonObject object = kCoreConstant.JSON_PARSER.parse(message).getAsJsonObject();
                    connections.get(object.get("type").getAsString()).accept(object);
                    return;
                }

                if (channel.equals("global")) {
                    try {
                        int index = message.indexOf("||");

                        Packet packet = kCoreConstant.GSON.fromJson(message.substring(index + 2), (Type) Class.forName(message.substring(0, index)));

                        if (packet.isAsync()) {
                            packet.onReceive();
                            return;
                        }

                        Bukkit.getScheduler().runTask(kCore.getInstance(), packet::onReceive);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }

        }, "global", "connection"))).start();

        RedisHandler.authenticate = authenticate;
        RedisHandler.password = password;
    }

    public static void close() {
        pool.close();
    }

    private static void runCommand(Consumer<Jedis> consumer) {
        Jedis jedis = pool.getResource();

        if (authenticate) {
            jedis.auth(password);
        }

        consumer.accept(jedis);
        jedis.close();
    }

    static void sendPacket(Packet packet) {
        CompletableFuture.runAsync(() -> runCommand(jedis -> jedis.publish("global", packet.getClass().getName() + "||" + kCoreConstant.GSON.toJson(packet))));
    }

    public static JedisPool getPool() {
        return pool;
    }

    public static Map<String, Consumer<JsonObject>> getConnections() {
        return connections;
    }

}