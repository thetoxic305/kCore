package me.vifez.core.staff.commands;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.kCore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class FlyCommand extends Command {

    private final kCore core;

    public FlyCommand(kCore core) {
        super("fly");

        this.core = core;

        setPermission("core.fly");
        setPlayerOnly(true);
        addAliases("flight");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0) {
            player.setAllowFlight(!player.getAllowFlight());
            sender.sendMessage(CC.YELLOW + "You are " + (player.getAllowFlight() ? CC.GREEN + "now" : CC.RED + "no longer") + CC.YELLOW + " able to fly.");
            return;
        }

        if (args.length == 1) {
            if (!sender.hasPermission("core.setfly")) {
                sender.sendMessage(kCoreConstant.NO_PERMISSION);
                return;
            }

            Player target = core.getServer().getPlayer(args[0]);

            if (target == null) {
                sender.sendMessage(kCoreConstant.playerNotOnline(args[0]));
                return;
            }

            target.setAllowFlight(!target.getAllowFlight());
            sender.sendMessage(target.getDisplayName() + " is " + (target.getAllowFlight() ? CC.GREEN + "now" : CC.RED + "no longer") + CC.YELLOW + " able to fly.");
            target.sendMessage(CC.YELLOW + "Your flight has been " + (target.getAllowFlight() ? CC.GREEN + "enabled" : CC.RED + "disabled") + CC.YELLOW + " by " + player.getDisplayName() + CC.YELLOW + '.');
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