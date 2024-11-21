package me.vifez.core.commands.gamemode;

import me.vifez.core.kCore;
import me.vifez.core.util.CC;
import me.vifez.core.util.command.Command;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdventureCommand extends Command {

    private final kCore core;

    public AdventureCommand(kCore core) {
        super("gma");
        this.core = core;
        addAliases("gamemode a", "adventure");
        setPermission("core.gamemode.adventure");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.RED + "Only players can use this command.");
            return;
        }

        Player player = (Player) sender;
        player.setGameMode(GameMode.ADVENTURE);
        player.sendMessage(CC.translate("&fYour gamemode has been set to &bADVENTURE"));
    }

    public void executeForOthers(CommandSender sender, Player target) {
        target.setGameMode(GameMode.ADVENTURE);
        target.sendMessage(CC.translate("&fYour gamemode has been set to &bADVENTURE."));
        sender.sendMessage(CC.translate("&fYou have set &b" + target.getName() + "'s &fgamemode to &bADVENTURE."));
    }
}