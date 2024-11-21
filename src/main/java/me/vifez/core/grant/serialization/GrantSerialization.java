package me.vifez.core.grant.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.vifez.core.grant.Grant;
import me.vifez.core.util.JsonSerialization;
import me.vifez.core.kCore;
import me.vifez.core.rank.Rank;

import java.util.UUID;

public class GrantSerialization implements JsonSerialization<Grant> {

    @Override
    public JsonObject serialize(Grant grant) {
        JsonObject object = new JsonObject();

        object.addProperty("rank", grant.getRank().getUUID().toString());
        object.addProperty("duration", grant.getDuration());

        object.addProperty("addedAt", grant.getAddedAt());
        object.addProperty("addedReason", grant.getAddedReason());
        object.addProperty("addedBy", grant.getAddedBy() == null ? null : grant.getAddedBy().toString());

        object.addProperty("removedAt", grant.getRemovedAt());
        object.addProperty("removedReason", grant.getRemovedReason());
        object.addProperty("removedBy", grant.getRemovedBy() == null ? null : grant.getRemovedBy().toString());

        return object;
    }

    @Override
    public Grant deserialize(JsonObject object) {
        JsonElement addedBy = object.get("addedBy");

        Rank rank = kCore.getInstance().getRankHandler().getRank(UUID.fromString(object.get("rank").getAsString()));

        if (rank == null) {
            return null;
        }

        Grant grant = new Grant(
                rank,
                object.get("duration").getAsLong(),

                object.get("addedAt").getAsLong(),
                object.get("addedReason").getAsString(),
                addedBy.isJsonNull() ? null : UUID.fromString(addedBy.getAsString())
        );

        JsonElement removedBy = object.get("removedBy");
        JsonElement removedReason = object.get("removedReason");
        grant.setRemoved(
                object.get("removedAt").getAsLong(),
                removedReason.isJsonNull() ? null : removedReason.getAsString(),
                removedBy.isJsonNull() ? null : UUID.fromString(removedBy.getAsString())
        );
        return grant;
    }

}