package me.vifez.core.staff.commands;

import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.util.PlayerUtil;
import me.vifez.core.kCore;
import me.vifez.core.profile.Profile;
import me.vifez.core.staff.StaffMode;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffChatCommand extends Command {

    private final kCore core;

    public StaffChatCommand(kCore core) {
        super("staffchat");

        this.core = core;

        setPermission("core.staff.chat");
        setPlayerOnly(true);
        addAliases("sc");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Profile profile = core.getProfileHandler().getProfile(PlayerUtil.getPlayer(sender).getUniqueId());

        if (args.length == 0) {
            profile.getStaffOptions().setStaffChat(!profile.getStaffOptions().isStaffChat());
            profile.save();

            sender.sendMessage(CC.YELLOW + "You are " + (profile.getStaffOptions().isStaffChat() ? CC.GREEN + "now" : CC.RED + "no longer") + CC.YELLOW + " speaking in staff chat.");

            if (profile.getStaffOptions().isStaffMode()) {
                Player player = PlayerUtil.getPlayer(sender);
                player.getInventory().setItem(7, profile.getStaffOptions().isStaffChat() ? StaffMode.STAFFCHAT_ENABLED.getItem(player) : StaffMode.STAFFCHAT_DISABLED.getItem(player));
                player.updateInventory();
            }
            return;
        }

        core.getStaffHandler().sendStaffChatMessage(profile, StringUtils.join(args));
    }

}