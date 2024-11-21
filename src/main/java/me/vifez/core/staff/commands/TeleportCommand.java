package me.vifez.core.staff.commands;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.kCore;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class TeleportCommand extends Command {

    private final kCore core;

    public TeleportCommand(kCore core) {
        super("teleport");

        this.core = core;

        addAliases("tp");
        setPermission("core.teleport");
        setPlayerOnly(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            Player target = core.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(kCoreConstant.playerNotOnline(args[0]));
                return;
            }
            Player player = (Player) sender;
            player.teleport(target);
            sender.sendMessage(CC.YELLOW + "Teleported to " + target.getDisplayName() + CC.YELLOW + '.');
            return;
        }

        if (args.length == 2) {
            if (!sender.hasPermission("core.admin")) {
                sender.sendMessage(kCoreConstant.NO_PERMISSION);
                return;
            }

            Player target = core.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(kCoreConstant.playerNotOnline(args[0]));
                return;
            }
            Player target1 = core.getServer().getPlayer(args[1]);
            if (target1 == null) {
                sender.sendMessage(kCoreConstant.playerNotOnline(args[1]));
                return;
            }
            Player player = (Player) sender;
            target.teleport(target1);
            target.sendMessage(CC.YELLOW + "You have been teleported to " + target1.getDisplayName() + CC.YELLOW + " by " + player.getDisplayName() + CC.YELLOW + '.');
            sender.sendMessage(CC.YELLOW + "Teleported " + target.getDisplayName() + CC.YELLOW + " to " + target1.getDisplayName() + CC.YELLOW + '.');
            return;
        }

        if (args.length == 3) {
            if (!NumberUtils.isNumber(args[0]) || !NumberUtils.isNumber(args[1]) || !NumberUtils.isNumber(args[2])) {
                sender.sendMessage(CC.RED + "X, Y and Z must be a double.");
                return;
            }

            double x = Double.parseDouble(args[0]) + 0.5;
            double y = Double.parseDouble(args[1]) + 0.5;
            double z = Double.parseDouble(args[2]) + 0.5;

            Player player = (Player) sender;
            player.teleport(new Location(player.getWorld(), x, y, z));

            sender.sendMessage(CC.YELLOW + "Teleported you to the specified location. (X: " + CC.WHITE + x + CC.YELLOW + ", Y: " + CC.WHITE + y + CC.YELLOW + ", Z: " + CC.WHITE + z + CC.YELLOW + ')');
            return;
        }

        if (args.length == 4) {
            if (!sender.hasPermission("core.admin")) {
                sender.sendMessage(kCoreConstant.NO_PERMISSION);
                return;
            }

            Player target = core.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(kCoreConstant.playerNotOnline(args[0]));
                return;
            }

            if (!NumberUtils.isNumber(args[1]) || !NumberUtils.isNumber(args[2]) || !NumberUtils.isNumber(args[3])) {
                sender.sendMessage(CC.RED + "X, Y and Z must be a double.");
                return;
            }

            double x = Double.parseDouble(args[1]) + 0.5;
            double y = Double.parseDouble(args[2]) + 0.5;
            double z = Double.parseDouble(args[3]) + 0.5;

            target.teleport(new Location(target.getWorld(), x, y, z));
            Player player = (Player) sender;
            sender.sendMessage(CC.YELLOW + "Teleported " + target.getDisplayName() + CC.YELLOW + " to the specified location. (X: " + CC.WHITE + x + CC.YELLOW + ", Y: " + CC.WHITE + y + CC.YELLOW + ", Z: " + CC.WHITE + z + CC.YELLOW + ')');
            target.sendMessage(CC.YELLOW + "You have been teleported to the specified location (X: " + CC.WHITE + x + CC.YELLOW + ", Y: " + CC.WHITE + y + CC.YELLOW + ", Z: " + CC.WHITE + z + CC.YELLOW + ") by " + player.getDisplayName() + CC.YELLOW + '.');
            return;
        }

        sender.sendMessage(CC.RED + "Usage: /teleport <player> [player2] or [player] <x> <y> <z>");
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1 || args.length == 2) {
            return getPlayerNames();
        }

        return Collections.emptyList();
    }

}