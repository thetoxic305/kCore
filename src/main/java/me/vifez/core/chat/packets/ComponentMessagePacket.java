package me.vifez.core.chat.packets;

import me.vifez.core.util.CC;
import me.vifez.core.util.redis.Packet;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ComponentMessagePacket extends Packet {

    private String message;
    private String permission;
    private String hover;

    public ComponentMessagePacket(String message, String hover) {
        this.message = message;
        this.hover = hover;
    }

    @Override
    public void onReceive() {
        TextComponent component = new TextComponent(CC.translate(message));

        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate(hover)).create()));

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (permission == null || player.hasPermission(permission)) {
                player.spigot().sendMessage(component);
            }
        }
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

}