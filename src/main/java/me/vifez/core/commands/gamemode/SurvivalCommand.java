package me.vifez.core.commands.gamemode;

import me.vifez.core.kCore;
import me.vifez.core.util.CC;
import me.vifez.core.util.command.Command;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SurvivalCommand extends Command {

    private final kCore core;

    public SurvivalCommand(kCore core) {
        super("gms");
        this.core = core;
        addAliases("gamemode s", "survival");
        setPermission("core.gamemode.survival");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.RED + "Only players can use this command.");
            return;
        }

        Player player = (Player) sender;
        player.setGameMode(GameMode.SURVIVAL);
        player.sendMessage(CC.translate("&fYour gamemode has been set to &bSURVIVAL"));
    }

    public void executeForOthers(CommandSender sender, Player target) {
        target.setGameMode(GameMode.SURVIVAL);
        target.sendMessage(CC.translate("&fYour gamemode has been set to &bSURVIVAL."));
        sender.sendMessage(CC.translate("&fYou have set &b" + target.getName() + "'s &fgamemode to &bSURVIVAL."));
    }
}