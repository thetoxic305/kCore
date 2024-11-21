package me.vifez.core.grant.commands;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.util.PlayerUtil;
import me.vifez.core.kCore;
import me.vifez.core.grant.menu.GrantsMenu;
import me.vifez.core.profile.Profile;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class GrantsCommand extends Command {

    private final kCore core;

    public GrantsCommand(kCore core) {
        super("grants");

        this.core = core;

        setPlayerOnly(true);
        setPermission("core.grants");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            Profile profile = core.getProfileHandler().getProfile(args[0]);

            if (profile == null) {
                sender.sendMessage(kCoreConstant.dataNotFound(args[0]));
                return;
            }
            new GrantsMenu(core, profile).open(PlayerUtil.getPlayer(sender));
            return;
        }

        sender.sendMessage(CC.RED + "Usage: /grants <player>");
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return getPlayerNames();
        }

        return Collections.emptyList();
    }

}