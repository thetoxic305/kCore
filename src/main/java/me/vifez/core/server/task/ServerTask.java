package me.vifez.core.server.task;

import me.vifez.core.kCore;
import me.vifez.core.profile.Profile;
import me.vifez.core.server.Server;
import me.vifez.core.server.ServerStatus;
import me.vifez.core.server.packets.ServerUpdatePacket;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.scheduler.BukkitRunnable;

public class ServerTask extends BukkitRunnable {

    private final kCore core;

    public ServerTask(kCore core) {
        this.core = core;
    }

    @Override
    public void run() {
        Server local = core.getServerHandler().getLocalServer();
        local.setStatus(core.getServer().hasWhitelist() ? ServerStatus.WHITELISTED : ServerStatus.ONLINE);
        local.setOnlinePlayers(core.getServer().getOnlinePlayers().size());
        local.setMaxPlayers(core.getServer().getMaxPlayers());

        int vanished = 0;
        for (Profile profile : core.getProfileHandler().getProfiles()) {
            if (!profile.getStaffOptions().isVanished()) {
                continue;
            }
            vanished++;
        }

        local.setVanishedPlayers(vanished);
        local.setTPS(MinecraftServer.getServer().recentTps);

        new ServerUpdatePacket(local).send();

        core.getServerHandler().getServers().removeIf(server -> server.getLastUpdate().getCurrentTime() > 5L && !server.equals(local));
    }

}