package me.vifez.core.profile.packets;

import me.vifez.core.kCore;
import me.vifez.core.profile.Profile;
import me.vifez.core.util.redis.Packet;
import org.bson.Document;

import java.util.UUID;

public class ProfileUpdatePacket extends Packet {

    private final UUID clientID = kCore.CLIENT_ID;

    private final String name;
    private final UUID uuid;
    private final Document document;

    private final ProfileUpdatePacketType type;

    public ProfileUpdatePacket(Profile profile, ProfileUpdatePacketType type) {
        this.name = profile.getName();
        this.uuid = profile.getUUID();
        this.document = profile.toDocument();
        this.type = type;

        setAsync(true);
    }

    @Override
    public void onReceive() {
        if (type != ProfileUpdatePacketType.CHECK_PUNISHMENTS) {
            if (clientID.equals(kCore.CLIENT_ID)) {
                return;
            }
        }

        Profile profile = kCore.getInstance().getProfileHandler().getProfile(uuid);

        if (profile == null) {
            profile = new Profile(name, uuid);
            kCore.getInstance().getProfileHandler().getProfiles().add(profile);
            return;
        }

        profile.update(document);

        if (profile.isOnline()) {
            if (type == ProfileUpdatePacketType.CHECK_GRANTS) {
                profile.setupPlayer();
                return;
            }

            if (type == ProfileUpdatePacketType.CHECK_PUNISHMENTS) {
                profile.checkPunishments();
            }
        }
    }

}