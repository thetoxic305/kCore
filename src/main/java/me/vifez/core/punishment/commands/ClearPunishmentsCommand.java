package me.vifez.core.punishment.commands;

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

public class ClearPunishmentsCommand extends Command {

    private final kCore core;

    public ClearPunishmentsCommand(kCore core) {
        super("clearpunishments");

        this.core = core;

        addAliases("clearhistory");
        setPermission("core.clearpunishments");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            Profile profile = core.getProfileHandler().getProfile(args[0]);

            if (profile == null) {
                sender.sendMessage(kCoreConstant.dataNotFound(args[0]));
                return;
            }

            if (profile.getPunishments().isEmpty()) {
                sender.sendMessage(CC.RED + profile.getName() + " does not have any punishments to clear.");
                return;
            }

            profile.getPunishments().clear();
            profile.save();
            new ProfileUpdatePacket(profile, ProfileUpdatePacketType.ONLY_UPDATE).send();
            sender.sendMessage(CC.YELLOW + "Cleared punishments of " + profile.getColoredName() + CC.YELLOW + '.');
            return;
        }

        sender.sendMessage(CC.RED + "Usage: /clearpunishments <player>");
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return getPlayerNames();
        }

        return Collections.emptyList();
    }

}
