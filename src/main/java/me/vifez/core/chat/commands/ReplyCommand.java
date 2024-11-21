package me.vifez.core.chat.commands;

import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.kCore;
import me.vifez.core.profile.Profile;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReplyCommand extends Command {

    private final kCore core;

    public ReplyCommand(kCore core) {
        super("reply");

        this.core = core;

        addAliases("r");
        setPlayerOnly(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 0) {
            Player player = (Player) sender;
            Profile profile = core.getProfileHandler().getProfile(player.getUniqueId());

            Player target = core.getServer().getPlayer(profile.getProfileOptions().getReplyTo());
            if (target == null) {
                sender.sendMessage(CC.RED + "The player you were in a conversation with has gone offline.");
                return;
            }

            Profile targetProfile = core.getProfileHandler().getProfile(target.getUniqueId());

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

            String message = StringUtils.join(args, " ", 0, args.length);

            player.sendMessage(CC.GRAY + "(To " + CC.WHITE + targetProfile.getColoredName() + CC.GRAY + "): " + CC.WHITE + message);
            target.sendMessage(CC.GRAY + "(From " + CC.WHITE + profile.getColoredName() + CC.GRAY + "): " + CC.WHITE + message);
            return;
        }

        sender.sendMessage(CC.RED + "Usage: /reply <message...>");
    }

}
