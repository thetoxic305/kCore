package me.vifez.core.profile.packets;

import me.vifez.core.util.CC;
import me.vifez.core.kCore;
import me.vifez.core.profile.Profile;
import me.vifez.core.util.redis.Packet;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ProfileMessagePacket extends Packet {

    private String name;
    private UUID uuid;
    private String message;

    public ProfileMessagePacket(Profile profile, String message) {
        this.name = profile.getName();
        this.uuid = profile.getUUID();
        this.message = message;
    }

    @Override
    public void onReceive() {
        Profile profile = kCore.getInstance().getProfileHandler().getProfile(uuid);

        if (profile == null) {
            profile = new Profile(name, uuid);
            kCore.getInstance().getProfileHandler().getProfiles().add(profile);
            return;
        }

        Player player = profile.getPlayer();
        if (player == null) {
            return;
        }

        player.sendMessage(CC.translate(message));
    }

}
