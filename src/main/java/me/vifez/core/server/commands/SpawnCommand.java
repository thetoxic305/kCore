package me.vifez.core.server.commands;

import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.kCore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand extends Command {

    private final kCore core;

    public SpawnCommand(kCore core) {
        super("spawn");

        this.core = core;

        setPlayerOnly(true);
        setPermission("core.spawn");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        player.teleport(core.getServerHandler().getSpawn());
        player.sendMessage(CC.YELLOW + "Teleported you to spawn.");
    }

}
