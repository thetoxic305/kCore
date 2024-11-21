package me.vifez.core.util;

import com.google.gson.JsonObject;

public interface JsonSerialization<T> {

    JsonObject serialize(T t);
    T deserialize(JsonObject object);

}
