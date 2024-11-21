package me.vifez.core.punishment.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.vifez.core.punishment.Punishment;
import me.vifez.core.punishment.PunishmentType;
import me.vifez.core.util.JsonSerialization;

import java.util.UUID;

public class PunishmentSerialization implements JsonSerialization<Punishment> {

    @Override
    public JsonObject serialize(Punishment punishment) {
        JsonObject object = new JsonObject();

        object.addProperty("type", punishment.getType().toString());
        object.addProperty("duration", punishment.getDuration());

        object.addProperty("addedAt", punishment.getAddedAt());
        object.addProperty("addedReason", punishment.getAddedReason());
        object.addProperty("addedBy", punishment.getAddedBy() == null ? null : punishment.getAddedBy().toString());

        object.addProperty("removedAt", punishment.getRemovedAt());
        object.addProperty("removedReason", punishment.getRemovedReason());
        object.addProperty("removedBy", punishment.getRemovedBy() == null ? null : punishment.getRemovedBy().toString());

        return object;
    }

    @Override
    public Punishment deserialize(JsonObject object) {
        JsonElement addedBy = object.get("addedBy");
        Punishment punishment = new Punishment(
                PunishmentType.valueOf(object.get("type").getAsString()),
                object.get("duration").getAsLong(),
                object.get("addedAt").getAsLong(),
                object.get("addedReason").getAsString(),
                addedBy.isJsonNull() ? null : UUID.fromString(addedBy.getAsString())
        );

        JsonElement removedBy = object.get("removedBy");
        JsonElement removedReason = object.get("removedReason");
        punishment.setRemoved(
                object.get("removedAt").getAsLong(),
                removedReason.isJsonNull() ? null : removedReason.getAsString(),
                removedBy.isJsonNull() ? null : UUID.fromString(removedBy.getAsString())
        );
        return punishment;
    }

}