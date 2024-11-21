package me.vifez.core.server.handler;

import me.vifez.core.kCore;
import me.vifez.core.kCoreConstant;
import me.vifez.core.handler.Handler;
import me.vifez.core.server.Server;
import me.vifez.core.server.warp.Warp;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashSet;
import java.util.Set;

public class ServerHandler extends Handler {

    private Location spawn;
    private Set<Warp> warps = new HashSet<>();

    private Server localServer;
    private Set<Server> servers = new HashSet<>();

    public ServerHandler(kCore core) {
        super(core);

        spawn = new Location(
                core.getServer().getWorld(core.getConfig().getString("spawn.location.world")),
                core.getConfig().getDouble("spawn.location.x"),
                core.getConfig().getDouble("spawn.location.y"),
                core.getConfig().getDouble("spawn.location.z"),
                (float) core.getConfig().getDouble("spawn.location.yaw"),
                (float) core.getConfig().getDouble("spawn.location.pitch")
        );

        ConfigurationSection warpSection = core.getConfig().getConfigurationSection("warps");

        if (warpSection != null) {
            for (String warpName : warpSection.getKeys(false)) {
                ConfigurationSection warpValues = warpSection.getConfigurationSection(warpName);

                Location location = new Location(
                        core.getServer().getWorld(warpValues.getString("world")),
                        warpValues.getDouble("x"),
                        warpValues.getDouble("y"),
                        warpValues.getDouble("z"),
                        (float) warpValues.getDouble("yaw"),
                        (float) warpValues.getDouble("pitch")
                );
                warps.add(new Warp(warpName, location));
            }
        }

        localServer = new Server(kCoreConstant.SERVER_NAME);
        servers.add(localServer);
    }

    public Warp getWarp(String name) {
        for (Warp warp : warps) {
            if (warp.getName().equals(name)) {
                return warp;
            }
        }
        return null;
    }

    public Server getServer(String name) {
        for (Server server : servers) {
            if (server.getName().equalsIgnoreCase(name)) {
                return server;
            }
        }
        return null;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;

        core.getConfig().set("spawn.location.world", spawn.getWorld().getName());
        core.getConfig().set("spawn.location.x", spawn.getX());
        core.getConfig().set("spawn.location.y", spawn.getY());
        core.getConfig().set("spawn.location.z", spawn.getZ());
        core.getConfig().set("spawn.location.yaw", spawn.getYaw());
        core.getConfig().set("spawn.location.pitch", spawn.getPitch());

        core.saveConfig();
    }

    public Set<Warp> getWarps() {
        return warps;
    }

    public Server getLocalServer() {
        return localServer;
    }

    public Set<Server> getServers() {
        return servers;
    }

}