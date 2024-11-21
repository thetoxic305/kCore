package me.vifez.core.grant.commands;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.kCore;
import me.vifez.core.profile.Profile;
import me.vifez.core.profile.packets.ProfileUpdatePacket;
import me.vifez.core.profile.packets.ProfileUpdatePacketType;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ClearGrantsCommand extends Command {

    private final kCore core;

    public ClearGrantsCommand(kCore core) {
        super("cleargrants");

        this.core = core;

        setPermission("core.grant.clear");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            Profile profile = core.getProfileHandler().getProfile(args[0]);

            if (profile == null) {
                sender.sendMessage(kCoreConstant.dataNotFound(args[0]));
                return;
            }

            profile.getGrants().clear();
            profile.save();
            profile.checkGrants();
            profile.setupPlayer();
            new ProfileUpdatePacket(profile, ProfileUpdatePacketType.CHECK_GRANTS).send();
            sender.sendMessage(CC.YELLOW + "Cleared grants of " + profile.getColoredName() + CC.YELLOW + '.');
            return;
        }

        sender.sendMessage(CC.RED + "Usage: /cleargrants <player>");
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return getPlayerNames();
        }
        return Collections.emptyList();
    }

}
