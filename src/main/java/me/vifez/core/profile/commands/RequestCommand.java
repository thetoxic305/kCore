package me.vifez.core.profile.commands;

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

public class RequestCommand extends Command {

    private final kCore core;

    public RequestCommand(kCore core) {
        super("request");

        this.core = core;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 0) {
            Player player = (Player) sender;
            Profile profile = core.getProfileHandler().getProfile(player.getUniqueId());

            Timer assistanceTimer = profile.getAssistanceTimer();
            if (assistanceTimer.getCurrentTime() != -1L) {
                sender.sendMessage(CC.RED + "You cannot request assistance/report players for another " +
                        DurationFormatUtils.formatDurationWords(assistanceTimer.getCurrentTime() * 1000, true, true) + '.');
                return;
            }

            String reason = StringUtils.join(args, " ", 0, args.length);

            MessagePacket packet = new MessagePacket(CC.GOLD + "[Request] " + profile.getColoredName() + CC.YELLOW + " has requested assistance" + CC.GRAY + ": " + CC.WHITE + reason);
            packet.setPermission("core.staff");
            packet.send();

            assistanceTimer.reset();
            sender.sendMessage(CC.YELLOW + "Your request has been sent.");
            return;
        }

        sender.sendMessage(CC.RED + "Usage: /request <reason...>");
    }

}