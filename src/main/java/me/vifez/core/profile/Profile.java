package me.vifez.core.profile;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mongodb.client.model.Filters;
import me.vifez.core.kCoreConstant;
import me.vifez.core.util.mongo.Collection;
import me.vifez.core.util.mongo.MongoHandler;
import me.vifez.core.util.mongo.subscribers.CommandSubscriber;
import me.vifez.core.util.Timer;
import me.vifez.core.kCore;
import me.vifez.core.grant.Grant;
import me.vifez.core.profile.packets.ProfileUpdatePacket;
import me.vifez.core.profile.packets.ProfileUpdatePacketType;
import me.vifez.core.punishment.Punishment;
import me.vifez.core.punishment.PunishmentType;
import me.vifez.core.rank.Rank;
import me.vifez.core.staff.StaffOptions;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.*;

public class Profile {

    public static final Collection COLLECTION = MongoHandler.getCollection("core", "profiles");

    private String name;
    private UUID uuid;

    private Rank rank;

    private final Set<String> addresses = new HashSet<>();
    private final Set<String> permissions = new HashSet<>();

    private final Set<Grant> grants = new HashSet<>();
    private final Set<Punishment> punishments = new HashSet<>();

    private ProfileOptions profileOptions = new ProfileOptions();
    private StaffOptions staffOptions = new StaffOptions(this);

    private Timer assistanceTimer = new Timer(-1L, 120L, true);

    public Profile(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public void save() {
        JsonArray grants = new JsonArray();
        for (Grant grant : this.grants) {
            grants.add(Grant.SERIALIZATION.serialize(grant));
        }

        JsonArray punishments = new JsonArray();
        for (Punishment punishment : this.punishments) {
            punishments.add(Punishment.SERIALIZATION.serialize(punishment));
        }

        Document document = new Document("name", name)
                .append("uuid", uuid.toString())
                .append("addresses", kCoreConstant.GSON.toJson(addresses))
                .append("permissions", kCoreConstant.GSON.toJson(permissions))
                .append("grants", grants.toString())
                .append("punishments", punishments.toString())
                .append("staffChat", staffOptions.isStaffChat());

        COLLECTION.async().replaceOne(Filters.eq("uuid", uuid.toString()), document, MongoHandler.REPLACE_OPTIONS).subscribe(new CommandSubscriber());
    }

    public void update(Document document) {
        addresses.clear();
        permissions.clear();
        punishments.clear();
        grants.clear();

        addresses.addAll(kCoreConstant.GSON.fromJson(document.getString("addresses"), kCoreConstant.SET_STRING_TYPE));
        permissions.addAll(kCoreConstant.GSON.fromJson(document.getString("permissions"), kCoreConstant.SET_STRING_TYPE));

        for (JsonElement grantElement : kCoreConstant.JSON_PARSER.parse(document.getString("grants")).getAsJsonArray()) {
            Grant grant = Grant.SERIALIZATION.deserialize(grantElement.getAsJsonObject());

            if (grant == null) {
                continue;
            }

            grants.add(grant);
        }

        for (JsonElement punishmentElement : kCoreConstant.JSON_PARSER.parse(document.getString("punishments")).getAsJsonArray()) {
            punishments.add(Punishment.SERIALIZATION.deserialize(punishmentElement.getAsJsonObject()));
        }

        staffOptions.setStaffChat(document.getBoolean("staffChat"));

        checkGrants();
    }

    public Document toDocument() {
        JsonArray grants = new JsonArray();
        for (Grant grant : this.grants) {
            grants.add(Grant.SERIALIZATION.serialize(grant));
        }

        JsonArray punishments = new JsonArray();
        for (Punishment punishment : this.punishments) {
            punishments.add(Punishment.SERIALIZATION.serialize(punishment));
        }

        Document document = new Document("name", name)
                .append("uuid", uuid.toString())
                .append("addresses", kCoreConstant.GSON.toJson(addresses))
                .append("permissions", kCoreConstant.GSON.toJson(permissions))
                .append("grants", grants.toString())
                .append("punishments", punishments.toString())
                .append("staffChat", staffOptions.isStaffChat());

        return document;
    }

    public boolean hasPermission(String permission) {
        for (Grant grant : grants) {
            if (!grant.isActive()) {
                continue;
            }

            if (grant.getRank().getAllPermissions().contains("*")) {
                return true;
            }

            if (grant.getRank().getAllPermissions().contains(permission)) {
                return true;
            }
        }

        return false;
    }

    public Set<Profile> getAlts() {
        Set<Profile> toReturn = new HashSet<>();

        for (Profile profile : kCore.getInstance().getProfileHandler().getProfiles()) {
            if (profile.equals(this)) {
                continue;
            }

            for (String address : profile.getAddresses()) {
                if (addresses.contains(address)) {
                    toReturn.add(profile);
                    break;
                }
            }
        }

        return toReturn;
    }

    public void setupPlayer() {
        Player player = getPlayer();

        if (player == null) {
            return;
        }

        for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
            if (attachmentInfo.getAttachment() == null || attachmentInfo.getAttachment().getPlugin() == null || !attachmentInfo.getAttachment().getPlugin().equals(kCore.getInstance())) {
                continue;
            }

            for (Map.Entry<String, Boolean> permissionEntry : attachmentInfo.getAttachment().getPermissions().entrySet()) {
                attachmentInfo.getAttachment().unsetPermission(permissionEntry.getKey());
            }
        }

        PermissionAttachment attachment = player.addAttachment(kCore.getInstance());

        if (rank != null) {
            for (Grant grant : grants) {
                if (grant.isActive()) {
                    for (String permission : grant.getRank().getAllPermissions()) {
                        attachment.setPermission(permission, true);
                    }
                }
            }
        }

        permissions.forEach(permission -> attachment.setPermission(permission, true));

        String coloredName = getColoredName();
        if (coloredName != null) {
            player.setDisplayName(coloredName);
            if (kCore.getInstance().getConfig().getBoolean("player.setTablistName")) {
                player.setPlayerListName(coloredName.substring(0, Math.min(coloredName.length(), 16)));
            }
        }
    }

    public void checkGrants() {
        if (grants.isEmpty()) {
            Grant grant = new Grant(
                    kCore.getInstance().getRankHandler().getDefaultRank(),
                    Long.MAX_VALUE,
                    System.currentTimeMillis(),
                    "Default Grant",
                    null
            );
            grants.add(grant);
            rank = grant.getRank();
            save();
        } else {
            List<Grant> grants = new ArrayList<>();
            for (Grant grant : this.grants) {
                if (!grant.isActive())
                    continue;

                grants.add(grant);
            }

            grants.sort(Comparator.comparingInt(grant -> grant.getRank().getPriority()));
            Collections.reverse(grants);

            rank = grants.get(0).getRank();
        }
    }

    public void checkPunishments() {
        Player player = getPlayer();

        for (PunishmentType type : Arrays.asList(PunishmentType.BLACKLIST, PunishmentType.BAN)) {
            if (type == PunishmentType.BAN && !kCore.getInstance().getConfig().getBoolean("punishment.disallow-ban-join")) {
                return;
            }

            Punishment punishment = getRelevantPunishment(type);
            if (punishment == null) {
                continue;
            }

            getAlts().forEach(alt -> new ProfileUpdatePacket(alt, ProfileUpdatePacketType.CHECK_PUNISHMENTS).send());

            Bukkit.getScheduler().runTask(kCore.getInstance(), () -> player.kickPlayer(kCore.getInstance().getPunishmentHandler().getMessage(punishment)));
            return;
        }

        for (Profile alt : getAlts()) {
            for (PunishmentType type : Arrays.asList(PunishmentType.BLACKLIST, PunishmentType.BAN)) {
                if (type == PunishmentType.BAN && !kCore.getInstance().getConfig().getBoolean("punishment.disallow-ban-join")) {
                    return;
                }

                Punishment punishment = alt.getRelevantPunishment(type);
                if (punishment == null) {
                    continue;
                }

                Bukkit.getScheduler().runTask(kCore.getInstance(), () -> player.kickPlayer(kCore.getInstance().getPunishmentHandler().getMessage(punishment, alt)));
                return;
            }
        }
    }

    public Punishment getRelevantPunishment(PunishmentType type) {
        for (Punishment punishment : this.punishments) {
            if (!punishment.isActive() || punishment.getType() != type) {
                continue;
            }
            return punishment;
        }
        return null;
    }

    public List<Grant> getSortedGrants() {
        List<Grant> toReturn = new ArrayList<>(grants);
        toReturn.sort(Comparator.comparingInt(grant -> (int) grant.getAddedAt()));
        Collections.reverse(toReturn);
        return toReturn;
    }

    public List<Punishment> getSortedPunishments() {
        List<Punishment> toReturn = new ArrayList<>(punishments);
        toReturn.sort(Comparator.comparingInt(punishment -> (int) punishment.getAddedAt()));
        Collections.reverse(toReturn);
        return toReturn;
    }

    public boolean isOnline() {
        return Bukkit.getPlayer(uuid) != null;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public String getColoredName() {
        if (rank == null) {
            return name;
        }
        return rank.getColor() + name;
    }

    public String getName() {
        return name;
    }

    public UUID getUUID() {
        return uuid;
    }

    public Rank getRank() {
        return rank;
    }

    public Set<String> getAddresses() {
        return addresses;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public Set<Grant> getGrants() {
        return grants;
    }

    public Set<Punishment> getPunishments() {
        return punishments;
    }

    public ProfileOptions getProfileOptions() {
        return profileOptions;
    }

    public StaffOptions getStaffOptions() {
        return staffOptions;
    }

    public Timer getAssistanceTimer() {
        return assistanceTimer;
    }
}