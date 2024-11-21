package me.vifez.core.profile.commands;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.util.PlayerUtil;
import me.vifez.core.kCore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class PingCommand extends Command {

    private final kCore core;

    public PingCommand(kCore core) {
        super("ping");

        this.core = core;

        setPlayerOnly(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            Player player = (Player) sender;
            sender.sendMessage(CC.YELLOW + "Your Ping" + CC.GRAY + ": " + CC.WHITE + PlayerUtil.getPing(player));
            return;
        }

        if (args.length == 1) {
            Player player = core.getServer().getPlayer(args[0]);

            if (player == null) {
                sender.sendMessage(kCoreConstant.playerNotOnline(args[0]));
                return;
            }

            sender.sendMessage(CC.YELLOW + "Ping of " + player.getDisplayName() + CC.GRAY + ": " + CC.WHITE + PlayerUtil.getPing(player));
            return;
        }

        sender.sendMessage(CC.RED + "Usage: /ping [player]");
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return getPlayerNames();
        }

        return Collections.emptyList();
    }

}