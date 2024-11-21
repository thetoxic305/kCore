package me.vifez.core.server.commands;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.kCore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class InventorySeeCommand extends Command {

    private final kCore core;

    public InventorySeeCommand(kCore core) {
        super("inventorysee");

        this.core = core;

        setPermission("core.invsee");
        addAliases("invsee", "inventoryview", "invvew");
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
            player.openInventory(target.getInventory());
            sender.sendMessage(CC.YELLOW + "Opened inventory of " + CC.WHITE + target.getDisplayName() + CC.YELLOW + '.');
            return;
        }

        sender.sendMessage(CC.RED + "Usage: /inventorysee <player>");
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return getPlayerNames();
        }

        return Collections.emptyList();
    }

}
