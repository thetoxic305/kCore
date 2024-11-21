package me.vifez.core.server.packets;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.redis.Packet;
import org.bukkit.Bukkit;

public class ServerRunCommandPacket extends Packet {

    private String server;
    private String command;

    public ServerRunCommandPacket(String server, String command) {
        this.server = server;
        this.command = command;
    }

    @Override
    public void onReceive() {
        if (!server.equals(kCoreConstant.SERVER_NAME) && !server.equalsIgnoreCase("all servers")) {
            return;
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

}