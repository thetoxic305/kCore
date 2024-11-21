package me.vifez.core.staff.commands;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.util.PlayerUtil;
import me.vifez.core.kCore;
import me.vifez.core.profile.Profile;
import me.vifez.core.staff.StaffMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class VanishCommand extends Command {

    private final kCore core;

    public VanishCommand(kCore core) {
        super("vanish");

        this.core = core;

        setPermission("core.staff.vanish");
        setPlayerOnly(true);
        addAliases("v", "hide", "byebye");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Profile profile = core.getProfileHandler().getProfile(PlayerUtil.getPlayer(sender).getUniqueId());

        if (args.length == 0) {
            profile.getStaffOptions().setVanished(!profile.getStaffOptions().isVanished());
            sender.sendMessage(CC.YELLOW + "You are " + (profile.getStaffOptions().isVanished() ? CC.GREEN + "now" : CC.RED + "no longer") + CC.YELLOW + " vanished.");

            if (profile.getStaffOptions().isStaffMode()) {
                Player player = PlayerUtil.getPlayer(sender);
                player.getInventory().setItem(8, profile.getStaffOptions().isVanished() ? StaffMode.VANISH_ENABLED.getItem(player) : StaffMode.VANISH_DISABLED.getItem(player));
                player.updateInventory();
            }
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
            target.getStaffOptions().setVanished(!profile.getStaffOptions().isVanished());
            sender.sendMessage(target.getColoredName() + " is " + (target.getStaffOptions().isVanished() ? CC.GREEN + "now" : CC.RED + "no longer") + CC.YELLOW + " vanished.");
            player.sendMessage(CC.YELLOW + "Your vanish has been " + (target.getStaffOptions().isVanished() ? CC.GREEN + "enabled" : CC.RED + "disabled") + CC.YELLOW + " by " + profile.getColoredName() + CC.YELLOW + '.');

            if (profile.getStaffOptions().isStaffMode()) {
                player.getInventory().setItem(8, target.getStaffOptions().isVanished() ? StaffMode.VANISH_ENABLED.getItem(player) : StaffMode.VANISH_DISABLED.getItem(player));
                player.updateInventory();
            }
            return;
        }

        sender.sendMessage(CC.RED + "Usage: /vanish [player]");
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1 && sender.hasPermission("core.owner")) {
            return getPlayerNames();
        }

        return Collections.emptyList();
    }

}