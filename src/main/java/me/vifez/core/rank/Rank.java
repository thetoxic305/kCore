package me.vifez.core.rank;

import com.mongodb.client.model.Filters;
import com.mongodb.reactivestreams.client.MongoCollection;
import me.vifez.core.kCoreConstant;
import me.vifez.core.util.mongo.MongoHandler;
import me.vifez.core.util.mongo.subscribers.CommandSubscriber;
import me.vifez.core.util.CC;
import me.vifez.core.kCore;
import me.vifez.core.profile.Profile;
import org.bson.Document;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Rank {

    public static final MongoCollection<Document> COLLECTION = MongoHandler.getCollection("core", "ranks").async();

    private String name;
    private final UUID uuid;

    private boolean _default;

    private int priority;
    private String color;
    private String prefix;
    private String suffix;

    private Set<String> permissions = new HashSet<>();

    private Set<Rank> inherits = new HashSet<>();
    private Set<UUID> inheritUUIDs = new HashSet<>();

    public Rank(String name, UUID uuid) {
        this(name, uuid, false, 0, "&f", "", "", Collections.emptySet(), Collections.emptySet());
    }

    public Rank(String name, UUID uuid, boolean _default, int priority, String color, String prefix, String suffix, Set<String> permissions, Set<UUID> inheritUUIDs) {
        this.name = name;
        this.uuid = uuid;
        this._default = _default;
        this.priority = priority;
        this.color = color;
        this.prefix = prefix;
        this.suffix = suffix;
        this.permissions.addAll(permissions);
        this.inheritUUIDs.addAll(inheritUUIDs);
    }

    public void save() {
        Set<String> inherits = new HashSet<>();
        for (Rank inherit : this.inherits) {
            inherits.add(inherit.getUUID().toString());
        }

        Document document = new Document("name", name)
                .append("uuid", uuid.toString())
                .append("default", _default)
                .append("priority", priority)
                .append("color", color)
                .append("prefix", prefix)
                .append("suffix", suffix)
                .append("permissions", kCoreConstant.GSON.toJson(permissions))
                .append("inherits", inherits.toString());

        COLLECTION.replaceOne(Filters.eq("uuid", uuid.toString()), document, MongoHandler.REPLACE_OPTIONS).subscribe(new CommandSubscriber());
    }

    public Document toDocument() {
        Set<String> inherits = new HashSet<>();
        for (Rank inherit : this.inherits) {
            inherits.add(inherit.getUUID().toString());
        }

        return new Document("name", name)
                .append("uuid", uuid.toString())
                .append("default", _default)
                .append("priority", priority)
                .append("color", color)
                .append("prefix", prefix)
                .append("suffix", suffix)
                .append("permissions", kCoreConstant.GSON.toJson(permissions))
                .append("inherits", inherits.toString());
    }

    public void update(Document document) {
        Set<String> inheritString = kCoreConstant.GSON.fromJson(document.getString("inherits"), kCoreConstant.SET_STRING_TYPE);
        Set<UUID> inherits = new HashSet<>();
        for (String string : inheritString) {
            inherits.add(UUID.fromString(string));
        }

        name = document.getString("name");
        _default = document.getBoolean("default");
        priority = (int) Math.round(document.getDouble("priority"));
        color = document.getString("color");
        prefix = document.getString("prefix");
        suffix = document.getString("suffix");
        permissions.addAll(kCoreConstant.GSON.fromJson(document.getString("permissions"), kCoreConstant.SET_STRING_TYPE));

        inheritUUIDs.addAll(inherits);
        loadInherits();
    }

    public void delete(boolean mongo) {
        for (Rank rank : kCore.getInstance().getRankHandler().getRanks()) {
            rank.getInherits().remove(this);

            if (mongo) {
                rank.save();
            }
        }

        for (Profile profile : kCore.getInstance().getProfileHandler().getProfiles()) {
            profile.getGrants().removeIf(grant -> grant.getRank().equals(this));
            profile.checkGrants();
            profile.setupPlayer();
            profile.save();
        }

        kCore.getInstance().getRankHandler().getRanks().remove(this);

        if (mongo) {
            COLLECTION.deleteOne(Filters.eq("uuid", uuid.toString())).subscribe(new CommandSubscriber());
        }
    }

    public Set<String> getAllPermissions() {
        Set<String> toReturn = new HashSet<>(permissions);
        for (Rank inherit : inherits) {
            toReturn.addAll(inherit.getAllPermissions());
        }
        return toReturn;
    }

    public void loadInherits() {
        for (UUID uuid : inheritUUIDs) {
            Rank rank = kCore.getInstance().getRankHandler().getRank(uuid);

            if (rank == null)
                continue;

            inherits.add(rank);
        }
    }

    public String getColoredName() {
        return CC.translate(color) + name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getDefaultDisplay() {
        return _default ? CC.GREEN + "Yes" : CC.RED + "No";
    }

    public boolean isDefault() {
        return _default;
    }

    public void setDefault(boolean _default) {
        this._default = _default;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getColor() {
        return CC.translate(color);
    }

    public String getDisplayColor() {
        return color.isEmpty() || color.equalsIgnoreCase("&f") ? "None" : getColor() + "Example";
    }

    public String getRawColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPrefix() {
        return CC.translate(prefix);
    }

    public String getDisplayPrefix() {
        return prefix.isEmpty() ? "None" : getPrefix();
    }

    public String getRawPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return CC.translate(suffix);
    }

    public String getDisplaySuffix() {
        return suffix.isEmpty() ? "None" : getSuffix();
    }

    public String getRawSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public Set<Rank> getInherits() {
        return inherits;
    }

}