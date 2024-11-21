package me.vifez.core.rank.packets;

import me.vifez.core.kCore;
import me.vifez.core.profile.Profile;
import me.vifez.core.util.redis.Packet;

public class RankPermissionPacket extends Packet {

    @Override
    public void onReceive() {
        for (Profile profile : kCore.getInstance().getProfileHandler().getProfiles()) {
            profile.setupPlayer();
        }
    }

}