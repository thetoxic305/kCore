package me.vifez.core.rank.packets;

import me.vifez.core.kCore;
import me.vifez.core.rank.Rank;
import me.vifez.core.util.redis.Packet;

import java.util.UUID;

public class RankCreatePacket extends Packet {

    private String name;
    private UUID uuid;

    public RankCreatePacket(Rank rank) {
        this.name = rank.getName();
        this.uuid = rank.getUUID();
    }

    @Override
    public void onReceive() {
        kCore.getInstance().getRankHandler().getRanks().add(new Rank(name, uuid));
    }

}