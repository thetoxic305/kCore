package me.vifez.core.commands.gamemode;

import me.vifez.core.kCore;
import me.vifez.core.util.CC;
import me.vifez.core.util.command.Command;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreativeCommand extends Command {

    private final kCore core;

    public CreativeCommand(kCore core) {
        super("gmc");
        this.core = core;
        addAliases("gamemode c", "creative");
        setPermission("core.gamemode.creative");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.RED + "Only players can use this command.");
            return;
        }

        Player player = (Player) sender;
        player.setGameMode(GameMode.CREATIVE);
        player.sendMessage(CC.translate("&fYour gamemode has been set to &bCREATIVE"));
    }

    public void executeForOthers(CommandSender sender, Player target) {
        target.setGameMode(GameMode.CREATIVE);
        target.sendMessage(CC.translate("&fYour gamemode has been set to &bCREATIVE."));
        sender.sendMessage(CC.translate("&fYou have set &b" + target.getName() + "'s &fgamemode to &bCREATIVE."));
    }
}