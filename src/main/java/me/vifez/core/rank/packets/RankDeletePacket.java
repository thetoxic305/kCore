package me.vifez.core.rank.packets;

import me.vifez.core.kCore;
import me.vifez.core.rank.Rank;
import me.vifez.core.util.redis.Packet;

import java.util.UUID;

public class RankDeletePacket extends Packet {

    private final UUID clientID = kCore.CLIENT_ID;

    private UUID uuid;

    public RankDeletePacket(Rank rank) {
        this.uuid = rank.getUUID();
    }

    @Override
    public void onReceive() {
        if (clientID.equals(kCore.CLIENT_ID)) {
            return;
        }

        Rank rank = kCore.getInstance().getRankHandler().getRank(uuid);

        if (rank == null) {
            return;
        }

        rank.delete(false);
    }

}