package me.vifez.core.chat.packets;

import me.vifez.core.util.CC;
import me.vifez.core.util.redis.Packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessagePacket extends Packet {

    private String message;
    private String permission;

    public MessagePacket(String message) {
        this.message = message;
    }

    @Override
    public void onReceive() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (permission == null || player.hasPermission(permission)) {
                player.sendMessage(CC.translate(message));
            }
        }
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

}