package me.vifez.core.util.serialization;

import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationSerialization {

    public static JsonObject serialize(Location location) {
        if (location == null) {
            return null;
        }

        JsonObject object = new JsonObject();

        object.addProperty("world", location.getWorld().getName());

        object.addProperty("x", location.getX());
        object.addProperty("y", location.getY());
        object.addProperty("z", location.getZ());

        object.addProperty("yaw", location.getYaw());
        object.addProperty("pitch", location.getPitch());

        return object;
    }

    public static Location deserialize(JsonObject object) {
        if (object == null || object.isJsonNull()) {
            return null;
        }

        return new Location(
                Bukkit.getWorld(object.get("world").getAsString()),

                object.get("x").getAsDouble(),
                object.get("y").getAsDouble(),
                object.get("z").getAsDouble(),

                object.get("yaw").getAsFloat(),
                object.get("pitch").getAsFloat()
        );
    }

}