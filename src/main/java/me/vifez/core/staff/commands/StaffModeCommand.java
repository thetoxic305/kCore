package me.vifez.core.staff.commands;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.util.PlayerUtil;
import me.vifez.core.kCore;
import me.vifez.core.profile.Profile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class StaffModeCommand extends Command {

    private final kCore core;

    public StaffModeCommand(kCore core) {
        super("staffmode");

        this.core = core;

        setPermission("core.staff.mode");
        setPlayerOnly(true);
        addAliases("staff", "mod", "modmode", "hackermode", "h");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Profile profile = core.getProfileHandler().getProfile(PlayerUtil.getPlayer(sender).getUniqueId());

        if (args.length == 0) {
            profile.getStaffOptions().setStaffMode(!profile.getStaffOptions().isStaffMode());
            sender.sendMessage(CC.YELLOW + "You are " + (profile.getStaffOptions().isStaffMode() ? CC.GREEN + "now" : CC.RED + "no longer") + CC.YELLOW + " in staff mode.");
            return;
        }

        if (args.length == 1) {
            if (!sender.hasPermission("core.owner")) {
                sender.sendMessage(kCoreConstant.NO_PERMISSION);
                return;
            }

            Player player = core.getServer().getPlayer(args[0]);

            if (player == null) {
                sender.sendMessage(kCoreConstant.playerNotOnline(args[0]));
                return;
            }

            Profile target = core.getProfileHandler().getProfile(player.getUniqueId());
            target.getStaffOptions().setStaffMode(!profile.getStaffOptions().isStaffMode());
            sender.sendMessage(target.getColoredName() + " is " + (target.getStaffOptions().isStaffMode() ? CC.GREEN + "now" : CC.RED + "no longer") + CC.YELLOW + " in staff mode.");
            player.sendMessage(CC.YELLOW + "Your staff mode has been " + (target.getStaffOptions().isStaffMode() ? CC.GREEN + "enabled" : CC.RED + "disabled") + CC.YELLOW + " by " + profile.getColoredName() + CC.YELLOW + '.');
            return;
        }

        sender.sendMessage(CC.RED + "Usage: /staffmode [player]");
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1 && sender.hasPermission("core.owner")) {
            return getPlayerNames();
        }

        return Collections.emptyList();
    }


}