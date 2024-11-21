package me.vifez.core.rank.packets;

import me.vifez.core.kCore;
import me.vifez.core.rank.Rank;
import me.vifez.core.util.redis.Packet;
import org.bson.Document;

import java.util.UUID;

public class RankReloadPacket extends Packet {

    private final UUID clientID = kCore.CLIENT_ID;

    private UUID uuid;
    private Document document;

    public RankReloadPacket(Rank rank) {
        this.uuid = rank.getUUID();
        this.document = rank.toDocument();
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

        rank.update(document);
    }

}