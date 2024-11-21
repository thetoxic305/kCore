package me.vifez.core.server.warp.commands;

import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.kCore;
import me.vifez.core.server.warp.Warp;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteWarpCommand extends Command {

    private final kCore core;

    public DeleteWarpCommand(kCore core) {
        super("deletewarp");

        this.core = core;

        addAliases("delwarp");
        setPermission("core.admin");
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
            warp.delete();
            sender.sendMessage(CC.YELLOW + "Deleted warp " + CC.WHITE + warp.getName() + CC.YELLOW + '.');
            return;
        }

        sender.sendMessage(CC.RED + "Usage: /deletewarp <warp>");
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
