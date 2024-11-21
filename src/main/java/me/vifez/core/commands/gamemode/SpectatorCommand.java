package me.vifez.core.commands.gamemode;

import me.vifez.core.kCore;
import me.vifez.core.util.CC;
import me.vifez.core.util.command.Command;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpectatorCommand extends Command {

    private final kCore core;

    public SpectatorCommand(kCore core) {
        super("gmsp");
        this.core = core;
        addAliases("gamemode sp", "spectator");
        setPermission("core.gamemode.spectator");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.RED + "Only players can use this command.");
            return;
        }

        Player player = (Player) sender;
        player.setGameMode(GameMode.SPECTATOR);
        player.sendMessage(CC.translate("&fYour gamemode has been set to &bSPECTATOR"));
    }

    public void executeForOthers(CommandSender sender, Player target) {
        target.setGameMode(GameMode.SPECTATOR);
        target.sendMessage(CC.translate("&fYour gamemode has been set to &bSPECTATOR."));
        sender.sendMessage(CC.translate("&fYou have set &b" + target.getName() + "'s &fgamemode to &bSPECTATOR."));
    }
}