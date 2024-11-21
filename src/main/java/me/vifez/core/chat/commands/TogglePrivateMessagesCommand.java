package me.vifez.core.chat.commands;

import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.util.PlayerUtil;
import me.vifez.core.kCore;
import me.vifez.core.profile.Profile;
import org.bukkit.command.CommandSender;

public class TogglePrivateMessagesCommand extends Command {

    private final kCore core;

    public TogglePrivateMessagesCommand(kCore core) {
        super("toggleprivatemessages");

        this.core = core;

        addAliases("tpm", "togglemessages", "tm");
        setPlayerOnly(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Profile profile = core.getProfileHandler().getProfile(PlayerUtil.getPlayer(sender).getUniqueId());

        profile.getProfileOptions().setReceivingPrivateMessages(!profile.getProfileOptions().isReceivingPrivateMessages());
        sender.sendMessage(CC.YELLOW + "You are " + (profile.getProfileOptions().isReceivingPrivateMessages() ? CC.GREEN + "now" : CC.RED + "no longer") + CC.YELLOW + " receiving private messages.");
    }

}