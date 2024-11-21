package me.vifez.core.staff.commands;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.kCore;
import me.vifez.core.profile.Profile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class FreezeCommand extends Command {

    private final kCore core;

    public FreezeCommand(kCore core) {
        super("freeze");

        this.core = core;

        addAliases("ss");
        setPermission("core.staff.freeze");
        setPlayerOnly(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            Player player = core.getServer().getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(kCoreConstant.playerNotOnline(args[0]));
                return;
            }

            Profile profile = core.getProfileHandler().getProfile(player.getUniqueId());
            profile.getProfileOptions().setFrozen(!profile.getProfileOptions().isFrozen());

            Player executor = (Player) sender;

            player.sendMessage(CC.YELLOW + "You have been " + (profile.getProfileOptions().isFrozen() ? CC.RED + "frozen" : CC.GREEN + "un-frozen") + CC.YELLOW + " by " + executor.getDisplayName() + CC.YELLOW + '.');
            sender.sendMessage(profile.getColoredName() + CC.YELLOW + " is " + (profile.getProfileOptions().isFrozen() ? CC.GREEN + "now" : CC.RED + "no longer") + CC.YELLOW + " frozen.");
            return;
        }

        sender.sendMessage(CC.RED + "Usage: /freeze <player>");
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return getPlayerNames();
        }

        return Collections.emptyList();
    }

}