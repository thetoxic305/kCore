package me.vifez.core.profile.commands;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.util.Timer;
import me.vifez.core.kCore;
import me.vifez.core.chat.packets.MessagePacket;
import me.vifez.core.profile.Profile;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ReportCommand extends Command {

    private final kCore core;

    public ReportCommand(kCore core) {
        super("report");

        this.core = core;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 1) {
            Player target = core.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(kCoreConstant.playerNotOnline(args[0]));
                return;
            }

            Player player = (Player) sender;

            if (target.equals(player)) {
                player.sendMessage(CC.RED + "You cannot report yourself.");
                return;
            }

            Profile profile = core.getProfileHandler().getProfile(player.getUniqueId());

            Timer assistanceTimer = profile.getAssistanceTimer();
            if (assistanceTimer.getCurrentTime() != -1L) {
                sender.sendMessage(CC.RED + "You cannot request assistance/report players for another " +
                        DurationFormatUtils.formatDurationWords(assistanceTimer.getCurrentTime() * 1000, true, true) + '.');
                return;
            }

            String reason = StringUtils.join(args, " ", 1, args.length);

            Profile targetProfile = core.getProfileHandler().getProfile(target.getUniqueId());

            MessagePacket packet = new MessagePacket(CC.GOLD + "[Report] " + profile.getColoredName() + CC.YELLOW + " has reported " + targetProfile.getColoredName() + CC.YELLOW + " for" + CC.GRAY + ": " + CC.WHITE + reason);
            packet.setPermission("core.staff");
            packet.send();

            assistanceTimer.reset();

            sender.sendMessage(CC.YELLOW + "Your report has been sent.");
            return;
        }

        sender.sendMessage(CC.RED + "Usage: /report <player> <reason...>");
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return getPlayerNames();
        }

        return Collections.emptyList();
    }

}