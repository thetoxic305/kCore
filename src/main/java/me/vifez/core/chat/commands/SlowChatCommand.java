package me.vifez.core.chat.commands;

import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.util.PlayerUtil;
import me.vifez.core.util.TimeUtil;
import me.vifez.core.kCore;
import me.vifez.core.profile.listener.ProfileListener;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.command.CommandSender;

public class SlowChatCommand extends Command {

    private final kCore core;

    public SlowChatCommand(kCore core) {
        super("slowchat");
        this.core = core;
        addAliases("slc", "delaychat", "dlc", "chatcooldown", "ccd");
        setPermission("core.chat.cooldown");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String executor = PlayerUtil.isPlayer(sender) ? PlayerUtil.getPlayer(sender).getDisplayName() : CC.DARK_RED + "Console";

        if (args.length != 1 && !core.getChatHandler().isDelay()) {
            sender.sendMessage(CC.translate(core.getMessage("chat.slowchat-no-delay")));
            sender.sendMessage(CC.RED + "Usage: /slowchat <delay>");
            return;
        }

        if (core.getChatHandler().isDelay()) {
            core.getChatHandler().setDelay(-1L);
            core.getServer().broadcastMessage(CC.translate(core.getMessage("chat.slowchat-unslowed").replace("{executor}", executor)));
            ProfileListener.chatCooldowns.clear();
            return;
        }

        long cooldown = TimeUtil.parseTime(args[0]);
        if (cooldown == -1L) {
            sender.sendMessage(CC.translate(core.getMessage("chat.invalid-cooldown")));
            sender.sendMessage(CC.RED + "Example: 2m30s (2 minutes, 30 seconds)");
            return;
        }

        core.getChatHandler().setDelay(cooldown / 1000);
        core.getServer().broadcastMessage(CC.translate(core.getMessage("chat.slowchat-slowed")
                .replace("{executor}", executor)
                .replace("{cooldown}", DurationFormatUtils.formatDurationWords(cooldown, true, true))));
    }
}