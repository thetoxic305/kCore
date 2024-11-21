package me.vifez.core.staff.commands;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.kCore;
import me.vifez.core.profile.Profile;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.*;

public class AltsCommand extends Command {

    private final kCore core;

    public AltsCommand(kCore core) {
        super("alts");
        this.core = core;
        setPermission("core.staff.alts");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            Profile profile = core.getProfileHandler().getProfile(args[0]);

            if (profile == null) {
                sender.sendMessage(kCoreConstant.dataNotFound(args[0]));
                return;
            }

            Set<Profile> alts = new HashSet<>(profile.getAlts());

            if (alts.isEmpty()) {
                sender.sendMessage(CC.translate(getMessage("alts.no-alts").replace("{player}", profile.getName())));
                return;
            }

            List<String> altNames = new ArrayList<>();
            for (Profile alt : profile.getAlts()) {
                altNames.add(alt.getColoredName());
            }

            sender.sendMessage(CC.translate(new String[]{
                    kCoreConstant.CHAT_LINE,
                    getMessage("alts.title").replace("{player}", profile.getColoredName()),
                    kCoreConstant.INDENT + StringUtils.join(altNames, CC.WHITE + ", "),
                    kCoreConstant.CHAT_LINE
            }));
            return;
        }

        sender.sendMessage(CC.RED + "Usage: /alts <player>");
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return getPlayerNames();
        }
        return Collections.emptyList();
    }

    private String getMessage(String path) {
        return core.getMessage(path);
    }
}