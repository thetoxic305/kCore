package me.vifez.core.chat.commands;

import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.util.PlayerUtil;
import me.vifez.core.kCore;
import org.bukkit.command.CommandSender;

public class MuteChatCommand extends Command {

    private final kCore core;

    public MuteChatCommand(kCore core) {
        super("mutechat");

        this.core = core;

        addAliases("mc");
        setPermission("core.chat.mute");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String executor = PlayerUtil.isPlayer(sender) ? PlayerUtil.getPlayer(sender).getDisplayName() : CC.DARK_RED + "Console";

        core.getChatHandler().setMuted(!core.getChatHandler().isMuted());

        String message = CC.translate(core.getMessages().getString(core.getChatHandler().isMuted() ? "chat.muted" : "chat.unmuted")
                .replace("{executor}", executor));

        core.getServer().broadcastMessage(message);
    }
}