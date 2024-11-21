package me.vifez.core.chat.commands;

import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.util.PlayerUtil;
import me.vifez.core.kCore;
import me.vifez.core.profile.Profile;
import org.bukkit.command.CommandSender;

public class ToggleChatCommand extends Command {

    private final kCore core;

    public ToggleChatCommand(kCore core) {
        super("togglechat");

        this.core = core;

        addAliases("tc", "toggleglobalchat", "tgc");
        setPlayerOnly(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Profile profile = core.getProfileHandler().getProfile(PlayerUtil.getPlayer(sender).getUniqueId());

        profile.getProfileOptions().setReceivingChatMessages(!profile.getProfileOptions().isReceivingChatMessages());
        sender.sendMessage(CC.YELLOW + "You are " + (profile.getProfileOptions().isReceivingChatMessages() ? CC.GREEN + "now" : CC.RED + "no longer") + CC.YELLOW + " receiving chat messages.");
    }

}