package me.vifez.core.punishment.commands;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.command.Command;
import me.vifez.core.util.*;
import me.vifez.core.kCore;
import me.vifez.core.chat.packets.ComponentMessagePacket;
import me.vifez.core.profile.Profile;
import me.vifez.core.profile.packets.ProfileMessagePacket;
import me.vifez.core.profile.packets.ProfileUpdatePacket;
import me.vifez.core.profile.packets.ProfileUpdatePacketType;
import me.vifez.core.punishment.Punishment;
import me.vifez.core.punishment.PunishmentType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class MuteCommand extends Command {

    private final kCore core;

    public MuteCommand(kCore core) {
        super("mute");

        this.core = core;

        setPermission("core.mute");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 1) {
            Profile profile = core.getProfileHandler().getProfile(args[0]);

            if (profile == null) {
                sender.sendMessage(kCoreConstant.dataNotFound(args[0]));
                return;
            }

            boolean silent = false;

            for (String arg : args) {
                if (arg.equalsIgnoreCase("-s")) {
                    silent = true;
                    break;
                }
            }

            long duration = TimeUtil.parseTime(args[1]);
            if (duration == -1L) {
                duration = Long.MAX_VALUE;
            }

            Punishment check = profile.getRelevantPunishment(PunishmentType.MUTE);
            if (check != null) {

                if (check.getExpiry() > duration) {
                    sender.sendMessage(CC.RED + profile.getName() + " is already muted with a longer duration.");
                    return;
                }

                check.setRemoved(System.currentTimeMillis(), "[System] Override");
            }

            int reasonIndex = duration == Long.MAX_VALUE ? 1 : 2;
            String reason = StringUtils.join(args, " ", reasonIndex, args.length).replaceAll("(?i)-s", "");

            Player player = PlayerUtil.getPlayer(sender);

            Punishment punishment = new Punishment(
                    PunishmentType.MUTE,
                    duration,
                    System.currentTimeMillis(),
                    reason,
                    player == null ? null : player.getUniqueId()
            );

            profile.getPunishments().add(punishment);
            profile.save();
            new ProfileUpdatePacket(profile, ProfileUpdatePacketType.ONLY_UPDATE).send();
            new ProfileMessagePacket(profile, core.getPunishmentHandler().getMessage(punishment)).send();

            Pair<String, String> message = core.getPunishmentHandler().getBroadcastMessage(profile, punishment, player == null ? null : player.getDisplayName(), silent);

            ComponentMessagePacket packet = new ComponentMessagePacket(message.getElementOne(), message.getElementTwo());
            if (silent) {
                packet.setPermission("core.mute");
            }
            packet.send();
            return;
        }

        sender.sendMessage(CC.RED + "Usage: /mute <player> [duration] <reason...> [-s]");
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return getPlayerNames();
        }

        return Collections.emptyList();
    }

}