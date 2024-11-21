package me.vifez.core.profile.listener;

import me.vifez.core.kCoreConstant;
import me.vifez.core.profile.Profile;
import me.vifez.core.util.CC;
import me.vifez.core.util.Timer;
import me.vifez.core.kCore;
import me.vifez.core.profile.packets.ProfileUpdatePacket;
import me.vifez.core.profile.packets.ProfileUpdatePacketType;
import me.vifez.core.punishment.Punishment;
import me.vifez.core.punishment.PunishmentType;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileListener implements Listener {

    private final kCore core;

    public static Map<UUID, Timer> chatCooldowns = new HashMap<>();

    public ProfileListener(kCore core) {
        this.core = core;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    private void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = core.getProfileHandler().getProfile(player.getUniqueId());

        Punishment punishment = profile.getRelevantPunishment(PunishmentType.MUTE);
        if (punishment != null) {
            player.sendMessage(core.getPunishmentHandler().getMessage(punishment));
            event.setCancelled(true);
            return;
        }

        if (!player.hasPermission("core.staff") && core.getChatHandler().isMuted()) {
            player.sendMessage(CC.RED + "You cannot speak as chat is currently muted.");
            event.setCancelled(true);
            return;
        }

        if (!player.hasPermission("core.staff") && core.getChatHandler().isDelay()) {
            Timer timer = chatCooldowns.get(player.getUniqueId());
            if (timer == null) {
                timer = new Timer(-1L, core.getChatHandler().getDelay(), true);
                chatCooldowns.put(player.getUniqueId(), timer);
                timer.reset();
            } else {
                if (timer.getCurrentTime() != -1L) {
                    player.sendMessage(CC.RED + "Chat is currently slowed, you cannot speak for another " + DurationFormatUtils.formatDurationWords(timer.getCurrentTime() * 1000, true, true) + '.');
                    event.setCancelled(true);
                    return;
                } else {
                    timer.reset();
                }
            }
        }

        if (player.hasPermission("core.staff") && profile.getStaffOptions().isStaffChat()) {
            core.getStaffHandler().sendStaffChatMessage(profile, event.getMessage());
            event.setCancelled(true);
            return;
        }

        if (!profile.getProfileOptions().isReceivingChatMessages()) {
            event.setCancelled(true);
            player.sendMessage(CC.RED + "You cannot send messages when you have chat disabled.");
            return;
        }

        for (Profile profile1 : core.getProfileHandler().getProfiles()) {
            if (!profile1.getProfileOptions().isReceivingChatMessages()) {
                event.getRecipients().remove(profile1.getPlayer());
            }
        }

        String suffix = profile.getRank().getSuffix().equals("") ? "" : " " + profile.getRank().getSuffix();
        event.setFormat(profile.getRank().getPrefix() + profile.getColoredName() + suffix + CC.GRAY + ": " + CC.WHITE + "%2$s");

        for (String word : core.getConfig().getStringList("blacklistedWords")) {
            if (event.getMessage().toLowerCase().contains(word.toLowerCase())) {
                event.getRecipients().removeAll(core.getServer().getOnlinePlayers());
                event.getRecipients().add(player);
            }
        }
    }

    @EventHandler
    private void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        String name = event.getName();
        UUID uuid = event.getUniqueId();

        Player player = core.getServer().getPlayer(uuid);
        if (player != null) {
            core.getServer().getScheduler().runTask(core, () -> player.kickPlayer(CC.RED + "Logged in from another location."));
            return;
        }

        Profile profile = core.getProfileHandler().getProfile(uuid);

        if (profile == null) {
            profile = new Profile(name, uuid);
            core.getProfileHandler().getProfiles().add(profile);
        }

        profile.getAddresses().add(event.getAddress().getHostAddress());
        profile.save();

        new ProfileUpdatePacket(profile, ProfileUpdatePacketType.ONLY_UPDATE).send();

        for (PunishmentType type : Arrays.asList(PunishmentType.BLACKLIST, PunishmentType.BAN)) {
            if (type == PunishmentType.BAN && !kCore.getInstance().getConfig().getBoolean("punishment.disallow-ban-join")) {
                return;
            }

            Punishment punishment = profile.getRelevantPunishment(type);
            if (punishment == null) {
                continue;
            }

            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(core.getPunishmentHandler().getMessage(punishment));
            return;
        }

        for (Profile alt : profile.getAlts()) {
            for (PunishmentType type : Arrays.asList(PunishmentType.BLACKLIST, PunishmentType.BAN)) {
                if (type == PunishmentType.BAN && !kCore.getInstance().getConfig().getBoolean("punishment.disallow-ban-join")) {
                    return;
                }

                Punishment punishment = alt.getRelevantPunishment(type);
                if (punishment == null) {
                    continue;
                }

                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                event.setKickMessage(core.getPunishmentHandler().getMessage(punishment, alt));
                return;
            }
        }
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();
        Profile profile = core.getProfileHandler().getProfile(player.getUniqueId());

        profile.checkGrants();
        profile.setupPlayer();

        for (String joinMessage : CC.translate(core.getConfig().getStringList("joinMessage"))) {
            player.sendMessage(joinMessage
                    .replace("%chatLine%", kCoreConstant.CHAT_LINE)
                    .replace("%indent%", kCoreConstant.INDENT)
            );
        }

        if (core.getConfig().getBoolean("spawn.join") || !player.hasPlayedBefore()) {
            player.teleport(core.getServerHandler().getSpawn());
        }

        for (Player player1 : core.getServer().getOnlinePlayers()) {
            if (player.hasPermission("core.staff"))
                player.showPlayer(player1);

            if (!player1.hasPermission("core.staff"))
                continue;

            Profile profile1 = core.getProfileHandler().getProfile(player1.getUniqueId());
            if (!profile1.getStaffOptions().isVanished())
                continue;

            player.hidePlayer(player1);
        }
    }

    @EventHandler
    private void onPlayerRespawn(PlayerRespawnEvent event) {
        event.setRespawnLocation(core.getServerHandler().getSpawn());
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        Player player = event.getPlayer();

        Profile profile = core.getProfileHandler().getProfile(player.getUniqueId());

        if (profile.getProfileOptions().isFrozen()) {
            core.getServer().broadcast(CC.GOLD + "[!] " + profile.getColoredName() + CC.YELLOW + " has logged out while frozen.", "core.staff");
        }

        if (profile.getStaffOptions().isStaffMode()) {
            profile.getStaffOptions().setStaffMode(false);
        }
    }

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Profile profile = core.getProfileHandler().getProfile(player.getUniqueId());
        if (!profile.getProfileOptions().isFrozen()) {
            return;
        }

        if (event.getFrom().getY() != event.getTo().getY() && event.getFrom().getX() == event.getTo().getX() && event.getFrom().getZ() == event.getTo().getZ()) {
            return;
        }

        if (event.getFrom().distanceSquared(event.getTo()) <= 0) {
            return;
        }

        player.teleport(event.getFrom());
    }

}