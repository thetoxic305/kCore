package me.vifez.core.staff.commands;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.kCore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class TeleportHereCommand extends Command {

    private final kCore core;

    public TeleportHereCommand(kCore core) {
        super("teleporthere");

        this.core = core;

        addAliases("tphere", "summon", "s");
        setPermission("core.tphere");
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

            target.teleport(player);

            sender.sendMessage(target.getDisplayName() + CC.YELLOW + " has been teleported to you.");
            target.sendMessage(CC.YELLOW + "You have been teleported to " + player.getDisplayName() + CC.YELLOW + '.');
            return;
        }

        sender.sendMessage(CC.RED + "Usage: /teleporthere <player>");
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return getPlayerNames();
        }

        return Collections.emptyList();
    }

}