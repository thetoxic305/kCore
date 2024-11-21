package me.vifez.core.staff.commands;

import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.kCore;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

import static org.bukkit.Bukkit.getName;

public class GamemodeCommand extends Command {

    private final kCore core;
    private final Map<String, GameMode> gamemodeAliases;

    public GamemodeCommand(kCore core) {
        super("gmc");
        this.core = core;

        setPermission("core.gamemode");
        setPlayerOnly(true);

        addAliases("gms", "gma", "gmsp");

        gamemodeAliases = new HashMap<>();
        gamemodeAliases.put("gmc", GameMode.CREATIVE);
        gamemodeAliases.put("gms", GameMode.SURVIVAL);
        gamemodeAliases.put("gma", GameMode.ADVENTURE);
        gamemodeAliases.put("gmsp", GameMode.SPECTATOR);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.RED + "Only players can change their gamemode.");
            return;
        }

        Player player = (Player) sender;
        String command = getName();

        GameMode gameMode = resolveGameMode(command);
        if (gameMode == null) {
            sender.sendMessage(CC.RED + "Gamemode not found.");
            return;
        }

        player.setGameMode(gameMode);
        player.sendMessage(CC.YELLOW + "Your gamemode has been set to " + CC.GOLD + gameMode.toString().toLowerCase() + CC.YELLOW + '.');
    }

    private GameMode resolveGameMode(String command) {
        return gamemodeAliases.get(command.toLowerCase());
    }
}