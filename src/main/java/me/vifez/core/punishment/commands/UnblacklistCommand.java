package me.vifez.core.punishment.commands;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.util.Pair;
import me.vifez.core.util.PlayerUtil;
import me.vifez.core.kCore;
import me.vifez.core.chat.packets.ComponentMessagePacket;
import me.vifez.core.profile.Profile;
import me.vifez.core.punishment.Punishment;
import me.vifez.core.punishment.PunishmentType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnblacklistCommand extends Command {

    private final kCore core;

    public UnblacklistCommand(kCore core) {
        super("unblacklist");

        this.core = core;

        setPermission("core.unblacklist");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (PlayerUtil.isPlayer(sender)) {
            sender.sendMessage(CC.RED + "This command can only be executed from console.");
            return;
        }

        if (args.length > 1) {
            Profile profile = core.getProfileHandler().getProfile(args[0]);

            if (profile == null) {
                sender.sendMessage(kCoreConstant.dataNotFound(args[0]));
                return;
            }

            Punishment punishment = profile.getRelevantPunishment(PunishmentType.BLACKLIST);

            if (punishment == null) {
                sender.sendMessage(CC.RED + profile.getName() + " is not blacklisted.");
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
            punishment.setRemoved(System.currentTimeMillis(), reason, player == null ? null : player.getUniqueId());

            profile.save();

            Pair<String, String> message = core.getPunishmentHandler().getRemovalBroadcastMessage(profile, punishment, player == null ? null : player.getDisplayName(), silent);
            ComponentMessagePacket packet = new ComponentMessagePacket(message.getElementOne(), message.getElementTwo());
            if (silent) {
                packet.setPermission("core.unblacklist");
            }
            packet.send();
            return;
        }

        sender.sendMessage(CC.RED + "Usage: /unmute <player> <reason...> [-s]");
    }

}