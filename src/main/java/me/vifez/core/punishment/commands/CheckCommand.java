package me.vifez.core.punishment.commands;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.util.PlayerUtil;
import me.vifez.core.kCore;
import me.vifez.core.profile.Profile;
import me.vifez.core.punishment.menu.CheckMenu;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class CheckCommand extends Command {

    private final kCore core;

    public CheckCommand(kCore core) {
        super("check");

        this.core = core;

        addAliases("history");
        setPermission("core.history");
        setPlayerOnly(true);
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
                sender.sendMessage(CC.RED + profile.getName() + " does not have any punishments to show.");
                return;
            }

            new CheckMenu(core, profile).open(PlayerUtil.getPlayer(sender));
            return;
        }

        sender.sendMessage(CC.RED + "Usage: /check <player>");
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return getPlayerNames();
        }

        return Collections.emptyList();
    }

}
