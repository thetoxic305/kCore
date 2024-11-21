package me.vifez.core.server.warp;

import me.vifez.core.kCore;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class Warp {

    private String name;
    private Location location;

    public Warp(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public void save() {
        FileConfiguration config = kCore.getInstance().getConfig();

        config.set("warps." + name + ".world", location.getWorld().getName());
        config.set("warps." + name + ".x", location.getX());
        config.set("warps." + name + ".y", location.getY());
        config.set("warps." + name + ".z", location.getZ());
        config.set("warps." + name + ".yaw", location.getYaw());
        config.set("warps." + name + ".pitch", location.getPitch());
        kCore.getInstance().saveConfig();
    }

    public void delete() {
        kCore.getInstance().getConfig().set("warps." + name, null);
        kCore.getInstance().saveConfig();
        kCore.getInstance().getServerHandler().getWarps().remove(this);
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

}