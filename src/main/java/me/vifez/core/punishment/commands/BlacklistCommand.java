package me.vifez.core.punishment.commands;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.util.Pair;
import me.vifez.core.util.PlayerUtil;
import me.vifez.core.kCore;
import me.vifez.core.chat.packets.ComponentMessagePacket;
import me.vifez.core.profile.Profile;
import me.vifez.core.profile.packets.ProfileUpdatePacket;
import me.vifez.core.profile.packets.ProfileUpdatePacketType;
import me.vifez.core.punishment.Punishment;
import me.vifez.core.punishment.PunishmentType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class BlacklistCommand extends Command {

    private final kCore core;

    public BlacklistCommand(kCore core) {
        super("blacklist");

        this.core = core;

        setPermission("core.blacklist");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 1) {
            Profile profile = core.getProfileHandler().getProfile(args[0]);

            if (profile == null) {
                sender.sendMessage(kCoreConstant.dataNotFound(args[0]));
                return;
            }

            if (profile.getRelevantPunishment(PunishmentType.BLACKLIST) != null) {
                sender.sendMessage(CC.RED + profile.getName() + " is already blacklisted.");
                return;
            }

            boolean silent = false;

            for (String arg : args) {
                if (arg.equalsIgnoreCase("-s")) {
                    silent = true;
                    break;
                }
            }

            String reason = StringUtils.join(args, " ", 1, args.length).replaceAll("(?i)-s", "");

            Player player = PlayerUtil.getPlayer(sender);

            Punishment punishment = new Punishment(
                    PunishmentType.BLACKLIST,
                    Long.MAX_VALUE,
                    System.currentTimeMillis(),
                    reason,
                    player == null ? null : player.getUniqueId()
            );

            profile.getPunishments().add(punishment);
            profile.save();
            new ProfileUpdatePacket(profile, ProfileUpdatePacketType.CHECK_PUNISHMENTS).send();

            Pair<String, String> message = core.getPunishmentHandler().getBroadcastMessage(profile, punishment, player == null ? null : player.getDisplayName(), silent);

            ComponentMessagePacket packet = new ComponentMessagePacket(message.getElementOne(), message.getElementTwo());
            if (silent) {
                packet.setPermission("core.blacklist");
            }
            packet.send();
            return;
        }

        sender.sendMessage(CC.RED + "Usage: /blacklist <player> <reason...> [-s]");
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return getPlayerNames();
        }

        return Collections.emptyList();
    }

}