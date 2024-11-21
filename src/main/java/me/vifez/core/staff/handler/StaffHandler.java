package me.vifez.core.staff.handler;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.redis.RedisHandler;
import me.vifez.core.util.CC;
import me.vifez.core.kCore;
import me.vifez.core.chat.packets.MessagePacket;
import me.vifez.core.handler.Handler;
import me.vifez.core.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.UUID;

public class StaffHandler extends Handler {

    public StaffHandler(kCore core) {
        super(core);

        setupConnections();
    }

    private void setupConnections() {
        RedisHandler.getConnections().put("connect", object -> {
            UUID uuid = UUID.fromString(object.get("uuid").getAsString());
            String server = object.get("server").getAsString();

            Profile profile = core.getProfileHandler().getProfile(uuid);
            if (profile == null) {
                return;
            }

            if (!profile.hasPermission("core.staff")) {
                return;
            }

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!onlinePlayer.hasPermission("core.staff")) {
                    continue;
                }

                onlinePlayer.sendMessage(CC.GOLD + "[Staff] " + profile.getColoredName() + CC.YELLOW + " has connected to " + CC.WHITE + server + CC.YELLOW + '.');
            }
        });

        RedisHandler.getConnections().put("switch", object -> {
            UUID uuid = UUID.fromString(object.get("uuid").getAsString());
            String from = object.get("from").getAsString();
            String to = object.get("to").getAsString();

            Profile profile = core.getProfileHandler().getProfile(uuid);
            if (profile == null) {
                return;
            }

            if (!profile.hasPermission("core.staff")) {
                return;
            }

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!onlinePlayer.hasPermission("core.staff")) {
                    continue;
                }

                onlinePlayer.sendMessage(CC.GOLD + "[Staff] " + profile.getColoredName() + CC.YELLOW + " has switched from " + CC.WHITE + from + CC.YELLOW + " to " + CC.WHITE + to + CC.YELLOW + '.');
            }
        });

        RedisHandler.getConnections().put("disconnect", object -> {
            UUID uuid = UUID.fromString(object.get("uuid").getAsString());
            String server = object.get("server").getAsString();

            Profile profile = core.getProfileHandler().getProfile(uuid);
            if (profile == null) {
                return;
            }

            if (!profile.hasPermission("core.staff")) {
                return;
            }

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!onlinePlayer.hasPermission("core.staff")) {
                    continue;
                }

                onlinePlayer.sendMessage(CC.GOLD + "[Staff] " + profile.getColoredName() + CC.YELLOW + " has disconnected from " + CC.WHITE + server + CC.YELLOW + '.');
            }
        });
    }

    public void sendStaffChatMessage(Profile profile, String message) {
        MessagePacket packet = new MessagePacket("&6[SC] &e[" + kCoreConstant.SERVER_NAME + "] " + profile.getColoredName() + "&7: &f" + message);
        packet.setPermission("core.staff");
        packet.send();
    }

    public GameMode getGamemode(String input) {

        if (input.toLowerCase().startsWith("c") || input.toLowerCase().equalsIgnoreCase("1")) {
            return GameMode.CREATIVE;
        }

        if (input.toLowerCase().startsWith("s") || input.toLowerCase().equalsIgnoreCase("0")) {
            return GameMode.SURVIVAL;
        }

        if (input.toLowerCase().startsWith("a") || input.toLowerCase().equalsIgnoreCase("2")) {
            return GameMode.ADVENTURE;
        }

        return null;
    }

}