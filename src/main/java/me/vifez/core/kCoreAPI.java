package me.vifez.core;

import me.vifez.core.profile.Profile;
import me.vifez.core.rank.Rank;
import me.vifez.core.server.Server;
import me.vifez.core.server.warp.Warp;
import org.bukkit.Location;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class kCoreAPI {

    private static kCore core;

    kCoreAPI(kCore core) {
        kCoreAPI.core = core;
    }

    public static UUID getClientID() {
        return kCore.CLIENT_ID;
    }

    public static Location getSpawn() {
        return core.getServerHandler().getSpawn();
    }

    public static void setSpawn(Location location) {
        core.getServerHandler().setSpawn(location);
    }

    public static Warp getWarp(String name) {
        return core.getServerHandler().getWarp(name);
    }

    public static Set<Warp> getWarps() {
        return core.getServerHandler().getWarps();
    }

    public static Profile getProfile(String name) {
        return core.getProfileHandler().getProfile(name);
    }

    public static Profile getProfile(UUID uuid) {
        return core.getProfileHandler().getProfile(uuid);
    }

    public static Set<Profile> getProfiles() {
        return core.getProfileHandler().getProfiles();
    }

    public static List<Profile> getSortedProfiles() {
        return core.getProfileHandler().getSortedProfiles();
    }

    public static Rank getRank(String name) {
        return core.getRankHandler().getRank(name);
    }

    public static Set<Rank> getRanks() {
        return core.getRankHandler().getRanks();
    }

    public static List<Rank> getSortedRanks() {
        return core.getRankHandler().getSortedRanks();
    }

    public static int getGlobalPlayers() {
        int i = 0;
        for (Server server : core.getServerHandler().getServers()) {
            i += server.getOnlinePlayers();
        }
        return i;
    }

    public static Server getLocalServer() {
        return core.getServerHandler().getLocalServer();
    }

    public static Server getServer(String name) {
        return core.getServerHandler().getServer(name);
    }

    public static Set<Server> getServers() {
        return core.getServerHandler().getServers();
    }

    public static boolean isChatMuted() {
        return core.getChatHandler().isMuted();
    }

    public static void setChatMuted(boolean muted) {
        core.getChatHandler().setMuted(muted);
    }

    public static boolean isChatDelayed() {
        return core.getChatHandler().isDelay();
    }

    public static long getChatDelay() {
        return core.getChatHandler().getDelay();
    }

    public static void setChatDelay(long delay) {
        core.getChatHandler().setDelay(delay);
    }

}