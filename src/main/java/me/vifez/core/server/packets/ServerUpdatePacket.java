package me.vifez.core.server.packets;

import me.vifez.core.kCore;
import me.vifez.core.server.Server;
import me.vifez.core.util.redis.Packet;
import me.vifez.core.server.ServerStatus;

import java.util.UUID;

public class ServerUpdatePacket extends Packet {

    private UUID senderID = kCore.CLIENT_ID;

    private String name;
    private ServerStatus status;
    private int onlinePlayers;
    private int maxPlayers;
    private int vanishedPlayers;
    private double[] tps;

    public ServerUpdatePacket(Server server) {
        this.name = server.getName();
        this.status = server.getStatus();
        this.onlinePlayers = server.getOnlinePlayers();
        this.maxPlayers = server.getMaxPlayers();
        this.vanishedPlayers = server.getVanishedPlayers();
        this.tps = server.getTPS();
    }

    @Override
    public void onReceive() {
        if (senderID.equals(kCore.CLIENT_ID)) {
            return;
        }

        Server server = kCore.getInstance().getServerHandler().getServer(name);

        if (server == null) {
            server = new Server(name, status, onlinePlayers, maxPlayers, vanishedPlayers, tps);
            kCore.getInstance().getServerHandler().getServers().add(server);
            return;
        }

        server.setStatus(status);
        server.setOnlinePlayers(onlinePlayers);
        server.setMaxPlayers(maxPlayers);
        server.setVanishedPlayers(vanishedPlayers);

        server.getLastUpdate().reset();
    }

}