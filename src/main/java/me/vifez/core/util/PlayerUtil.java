package me.vifez.core.util;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.InvocationTargetException;

public class PlayerUtil {

    private static final String NAMETAG_PREFIX = "nt_team_";

    public static int getPing(Player player) {
        try {
            EntityPlayer entityPlayer = (EntityPlayer) player.getClass().getMethod("getHandle").invoke(player);
            return (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void setNametagColor(Player player, Player other, ChatColor color) {
        Scoreboard scoreboard = player.getScoreboard();

        if (scoreboard.equals(Bukkit.getServer().getScoreboardManager().getMainScoreboard())) {
            scoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        }

        Team team = player.getScoreboard().getTeam(getTeamName(color));

        if (team == null) {
            team = player.getScoreboard().registerNewTeam(getTeamName(color));
            team.setPrefix(color.toString());
        }

        if (!team.hasEntry(other.getName())) {
            resetNametag(player, other);
            team.addEntry(other.getName());
        }

        player.setScoreboard(scoreboard);
    }

    public static void resetNametag(Player player, Player other) {
        if (player != null && other != null && !player.equals(other)) {
            for (ChatColor chatColor : ChatColor.values()) {
                Team team = player.getScoreboard().getTeam(getTeamName(chatColor));

                if (team != null) {
                    team.removeEntry(other.getName());
                }
            }
        }
    }

    private static String getTeamName(ChatColor color) {
        return NAMETAG_PREFIX + color.ordinal();
    }

    public static boolean isPlayer(Object object) {
        return object instanceof Player;
    }

    public static Player getPlayer(CommandSender sender) {
        return sender instanceof Player ? (Player) sender : null;
    }

    public static Player getPlayerByName(String name) {
        return Bukkit.getPlayer(name);
    }
}