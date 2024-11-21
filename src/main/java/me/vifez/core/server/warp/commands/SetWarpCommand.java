package me.vifez.core.server.warp.commands;

import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.kCore;
import me.vifez.core.server.warp.Warp;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarpCommand extends Command {

    private final kCore core;

    public SetWarpCommand(kCore core) {
        super("setwarp");

        this.core = core;

        setPermission("core.admin");
        setPlayerOnly(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            Warp warp = core.getServerHandler().getWarp(args[0]);
            if (warp != null) {
                sender.sendMessage(CC.RED + "A warp with name " + warp.getName() + " already exists.");
                return;
            }
            Player player = (Player) sender;
            warp = new Warp(args[0], player.getLocation());
            warp.save();
            core.getServerHandler().getWarps().add(warp);
            sender.sendMessage(CC.YELLOW + "Set warp " + CC.WHITE + warp.getName() + CC.YELLOW + '.');
            return;
        }

        sender.sendMessage(CC.RED + "Usage: /setwarp <name>");
    }

}
