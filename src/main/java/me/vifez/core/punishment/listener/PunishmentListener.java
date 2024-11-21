package me.vifez.core.punishment.listener;

import me.vifez.core.punishment.Punishment;
import me.vifez.core.punishment.PunishmentType;
import me.vifez.core.util.Pair;
import me.vifez.core.kCore;
import me.vifez.core.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PunishmentListener implements Listener {

    private final kCore core;

    public PunishmentListener(kCore core) {
        this.core = core;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    private void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission("core.staff")) {
            return;
        }

        String message = event.getMessage();

        if (core.getPunishmentHandler().getRemoving().containsKey(player.getUniqueId())) {
            event.setCancelled(true);

            Pair<Profile, Punishment> pair = core.getPunishmentHandler().getRemoving().get(player.getUniqueId());
            Profile profile = pair.getElementOne();
            Punishment punishment = pair.getElementTwo();

            core.getServer().getScheduler().runTask(core, () -> player.performCommand((punishment.getType() == PunishmentType.BAN ? "unban" : "unmute") + ' ' + profile.getName() + ' ' + message));
            core.getPunishmentHandler().getRemoving().remove(player.getUniqueId());
        }

    }

}
