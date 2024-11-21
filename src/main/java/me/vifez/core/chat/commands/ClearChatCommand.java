package me.vifez.core.chat.commands;

import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.util.PlayerUtil;
import me.vifez.core.kCore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearChatCommand extends Command {

    private final kCore core;

    public ClearChatCommand(kCore core) {
        super("clearchat");
        this.core = core;
        addAliases("cc");
        setPermission("core.chat.clear");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String executor = PlayerUtil.isPlayer(sender)
                ? PlayerUtil.getPlayer(sender).getDisplayName()
                : CC.DARK_RED + "Console";

        for (Player player : core.getServer().getOnlinePlayers()) {
            for (int i = 0; i < 100; i++) {
                player.sendMessage(" ");
            }
        }

        core.getServer().broadcastMessage(CC.translate(core.getMessage("chat.cleared")
                .replace("{executor}", executor)));
    }
}