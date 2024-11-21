package me.vifez.core.grant.listener;

import me.vifez.core.grant.Grant;
import me.vifez.core.kCoreConstant;
import me.vifez.core.util.CC;
import me.vifez.core.util.Pair;
import me.vifez.core.util.TimeUtil;
import me.vifez.core.kCore;
import me.vifez.core.profile.Profile;
import me.vifez.core.profile.packets.ProfileUpdatePacket;
import me.vifez.core.profile.packets.ProfileUpdatePacketType;
import me.vifez.core.rank.Rank;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GrantListener implements Listener {

    private final kCore core;

    private Map<UUID, String> reason = new HashMap<>();

    public GrantListener(kCore core) {
        this.core = core;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    private void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        String message = event.getMessage();

        if (core.getGrantHandler().getRemoving().containsKey(player.getUniqueId())) {
            event.setCancelled(true);

            Pair<Profile, Grant> pair = core.getGrantHandler().getRemoving().get(player.getUniqueId());
            Profile profile = pair.getElementOne();
            Grant grant = pair.getElementTwo();
            grant.setRemoved(System.currentTimeMillis(), event.getMessage(), player.getUniqueId());

            profile.save();
            profile.checkGrants();
            profile.setupPlayer();
            new ProfileUpdatePacket(profile, ProfileUpdatePacketType.CHECK_GRANTS).send();
            player.sendMessage(CC.YELLOW + "Removed a grant from " + profile.getColoredName() + CC.YELLOW + '.');
            core.getGrantHandler().getRemoving().remove(player.getUniqueId());
            return;
        }

        if (reason.containsKey(player.getUniqueId())) {
            long duration = TimeUtil.parseTime(message);

            if (message.equalsIgnoreCase("permanent") || message.equalsIgnoreCase("perm")) {
                duration = Long.MAX_VALUE;
            }

            if (duration == -1L) {
                player.sendMessage(CC.RED + "Invalid duration provided.");
                return;
            }

            Rank rank = core.getGrantHandler().getGrantRank().get(player.getUniqueId());
            String addedReason = reason.get(player.getUniqueId());
            Profile profile = core.getGrantHandler().getGrantProfile().get(player.getUniqueId());

            if (addedReason.equalsIgnoreCase("Default Grant")) {
                player.sendMessage(CC.RED + "Invalid reason.");
                return;
            }

            Grant grant = new Grant(
                    rank,
                    duration,
                    System.currentTimeMillis(),
                    addedReason,
                    player.getUniqueId()
            );

            profile.getGrants().add(grant);
            profile.save();
            profile.checkGrants();
            profile.setupPlayer();
            new ProfileUpdatePacket(profile, ProfileUpdatePacketType.CHECK_GRANTS).send();

            reason.remove(player.getUniqueId());
            core.getGrantHandler().getGrantProfile().remove(player.getUniqueId());
            core.getGrantHandler().getGrantRank().remove(player.getUniqueId());

            event.setCancelled(true);

            player.sendMessage(new String[]{
                    kCoreConstant.CHAT_LINE,
                    CC.GOLD + "Grant Applied",
                    kCoreConstant.INDENT + CC.YELLOW + "Player" + CC.GRAY + ": " + CC.WHITE + profile.getColoredName(),
                    kCoreConstant.INDENT + CC.YELLOW + "Executor" + CC.GRAY + ": " + CC.WHITE + player.getDisplayName(),
                    kCoreConstant.INDENT + CC.YELLOW + "Reason" + CC.GRAY + ": " + CC.WHITE + addedReason,
                    kCoreConstant.INDENT + CC.YELLOW + "Duration" + CC.GRAY + ": " + CC.WHITE + grant.getDurationText(),
                    kCoreConstant.CHAT_LINE
            });
            return;
        }

        if (core.getGrantHandler().getGrantRank().containsKey(player.getUniqueId())) {
            reason.put(player.getUniqueId(), event.getMessage());

            player.sendMessage(new String[]{
                    CC.YELLOW + "Please enter a duration for this grant.",
                    kCoreConstant.INDENT + CC.YELLOW + "Example" + CC.GRAY + ": " + CC.WHITE + "2w2d " + CC.YELLOW + "(2 weeks, 2 days) or" + CC.WHITE + " permanent" + CC.YELLOW + '.'
            });
            event.setCancelled(true);
        }
    }
}