package me.vifez.core.server.warp.commands;

import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.kCore;
import me.vifez.core.server.warp.Warp;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WarpCommand extends Command {

    private final kCore core;

    public WarpCommand(kCore core) {
        super("warp");

        this.core = core;

        setPermission("core.staff");
        setPlayerOnly(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            Warp warp = core.getServerHandler().getWarp(args[0]);
            if (warp == null) {
                sender.sendMessage(CC.RED + "No warp with name " + args[0] + " found.");
                return;
            }
            Player player = (Player) sender;
            player.teleport(warp.getLocation());
            sender.sendMessage(CC.YELLOW + "Teleported you to warp " + CC.WHITE + warp.getName() + CC.YELLOW + '.');
            return;
        }

        sender.sendMessage(CC.RED + "Usage: /warp <warp>");
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> toReturn = new ArrayList<>();
            for (Warp warp : core.getServerHandler().getWarps()) {
                toReturn.add(warp.getName());
            }
            return toReturn;
        }

        return Collections.emptyList();
    }

}
