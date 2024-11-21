package me.vifez.core.chat.commands;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.kCore;
import me.vifez.core.profile.Profile;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class MessageCommand extends Command {

    private final kCore core;

    public MessageCommand(kCore core) {
        super("message");

        this.core = core;

        addAliases("msg", "m", "tell", "t", "whisper", "w");
        setPlayerOnly(true);
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

            if (player.equals(target)) {
                sender.sendMessage(CC.RED + "You cannot message yourself.");
                return;
            }

            Profile targetProfile = core.getProfileHandler().getProfile(target.getUniqueId());
            Profile profile = core.getProfileHandler().getProfile(player.getUniqueId());

            if (profile.getProfileOptions().getIgnored().contains(target.getUniqueId())) {
                sender.sendMessage(CC.RED + "You cannot private message " + target.getName() + " as you have them ignored.");
                return;
            }

            if (targetProfile.getProfileOptions().getIgnored().contains(player.getUniqueId())) {
                sender.sendMessage(CC.RED + "You cannot private message " + target.getName() + " as they have ignored you.");
                return;
            }

            if (!profile.getProfileOptions().isReceivingPrivateMessages()) {
                sender.sendMessage(CC.RED + "You cannot send messages while you have private messages disabled.");
                return;
            }


            if (!targetProfile.getProfileOptions().isReceivingPrivateMessages()) {
                sender.sendMessage(CC.RED + targetProfile.getName() + " has private messages disabled.");
                return;
            }

            profile.getProfileOptions().setReplyTo(target.getUniqueId());
            targetProfile.getProfileOptions().setReplyTo(player.getUniqueId());

            String message = StringUtils.join(args, " ", 1, args.length);

            player.sendMessage(CC.GRAY + "(To " + CC.WHITE + targetProfile.getColoredName() + CC.GRAY + "): " + CC.WHITE + message);
            target.sendMessage(CC.GRAY + "(From " + CC.WHITE + profile.getColoredName() + CC.GRAY + "): " + CC.WHITE + message);
            return;
        }

        sender.sendMessage(CC.RED + "Usage: /message <player> <message...>");
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return getPlayerNames();
        }

        return Collections.emptyList();
    }

}
