package me.vifez.core.profile.handler;

import com.google.gson.JsonElement;
import me.vifez.core.kCoreConstant;
import me.vifez.core.profile.Profile;
import me.vifez.core.util.mongo.subscribers.RequestSubscriber;
import me.vifez.core.kCore;
import me.vifez.core.grant.Grant;
import me.vifez.core.handler.Handler;
import me.vifez.core.punishment.Punishment;
import org.bson.Document;

import java.util.*;

public class ProfileHandler extends Handler {

    private Set<Profile> profiles = new HashSet<>();

    public ProfileHandler(kCore core) {
        super(core);
    }

    public void loadProfiles() {
        Profile.COLLECTION.async().find().subscribe(new RequestSubscriber(Long.MAX_VALUE) {
            @Override
            public void onNext(Document document) {
                String name = document.getString("name");
                UUID uuid = UUID.fromString(document.getString("uuid"));

                Profile profile = new Profile(name, uuid);

                profile.getAddresses().addAll(kCoreConstant.GSON.fromJson(document.getString("addresses"), kCoreConstant.SET_STRING_TYPE));
                profile.getPermissions().addAll(kCoreConstant.GSON.fromJson(document.getString("addresses"), kCoreConstant.SET_STRING_TYPE));

                for (JsonElement grantElement : kCoreConstant.JSON_PARSER.parse(document.getString("grants")).getAsJsonArray()) {
                    Grant grant = Grant.SERIALIZATION.deserialize(grantElement.getAsJsonObject());

                    if (grant == null) {
                        continue;
                    }

                    profile.getGrants().add(grant);
                }

                for (JsonElement punishmentElement : kCoreConstant.JSON_PARSER.parse(document.getString("punishments")).getAsJsonArray()) {
                    profile.getPunishments().add(Punishment.SERIALIZATION.deserialize(punishmentElement.getAsJsonObject()));
                }

                profile.getStaffOptions().setStaffChat(document.getBoolean("staffChat"));

                profile.checkGrants();

                profiles.add(profile);
            }
        });
    }

    public Profile getProfile(String name) {
        for (Profile profile : profiles) {
            if (profile.getName().toLowerCase().startsWith(name.toLowerCase())) {
                return profile;
            }
        }
        return null;
    }

    public Profile getProfile(UUID uuid) {
        for (Profile profile : profiles) {
            if (profile.getUUID().equals(uuid)) {
                return profile;
            }
        }
        return null;
    }

    public List<Profile> getSortedProfiles() {
        List<Profile> toReturn = new ArrayList<>(profiles);
        toReturn.sort(Comparator.comparingInt(profile -> profile.getRank().getPriority()));
        Collections.reverse(toReturn);
        return toReturn;
    }

    public Set<Profile> getProfiles() {
        return profiles;
    }

}