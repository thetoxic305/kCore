package me.vifez.core.server.commands;

import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.kCore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand extends Command {

    private final kCore core;

    public SetSpawnCommand(kCore core) {
        super("setspawn");

        this.core = core;

        setPlayerOnly(true);
        setPermission("core.setspawn");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        core.getServerHandler().setSpawn(player.getLocation());
        player.sendMessage(CC.YELLOW + "Set the spawn point to your current location.");
    }

}
