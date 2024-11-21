package me.vifez.core.profile.commands;

import me.vifez.core.kCoreConstant;
import me.vifez.core.profile.Profile;
import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.kCore;
import me.vifez.core.profile.packets.ProfileUpdatePacket;
import me.vifez.core.profile.packets.ProfileUpdatePacketType;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PermissionsCommand extends Command {

    private final kCore core;

    public PermissionsCommand(kCore core) {
        super("permissions");

        this.core = core;

        setPermission("core.owner");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            printHelp(sender);
            return;
        }

        if (args[0].equalsIgnoreCase("list")) {
            if (args.length != 2) {
                sender.sendMessage(CC.RED + "Usage: /permission list <player>");
                return;
            }

            Profile profile = core.getProfileHandler().getProfile(args[1]);

            if (profile == null) {
                sender.sendMessage(kCoreConstant.dataNotFound(args[1]));
                return;
            }

            if (profile.getPermissions().isEmpty()) {
                sender.sendMessage(CC.RED + profile.getName() + " does not have any permissions to list.");
                return;
            }

            sender.sendMessage(new String[]{
                    kCoreConstant.CHAT_LINE,
                    CC.GOLD + "Permissions of " + profile.getColoredName()
            });
            profile.getPermissions().forEach(permission -> sender.sendMessage(kCoreConstant.INDENT + CC.WHITE + permission));
            sender.sendMessage(kCoreConstant.CHAT_LINE);
            return;
        }

        if (args[0].equalsIgnoreCase("add")) {
            if (args.length != 3) {
                sender.sendMessage(CC.RED + "Usage: /permission add <player> <permission>");
                return;
            }

            Profile profile = core.getProfileHandler().getProfile(args[1]);

            if (profile == null) {
                sender.sendMessage(kCoreConstant.dataNotFound(args[1]));
                return;
            }

             String permission = args[1].toLowerCase();

            if (profile.getPermissions().contains(permission)) {
                sender.sendMessage(CC.RED + profile.getName() + " already has permission " + permission + '.');
                return;
            }

            profile.getPermissions().add(permission);
            profile.save();
            new ProfileUpdatePacket(profile, ProfileUpdatePacketType.CHECK_GRANTS).send();

            sender.sendMessage(CC.YELLOW + "Added permission " + CC.WHITE + permission + CC.YELLOW + " to " + profile.getColoredName() + CC.YELLOW + '.');
            return;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (args.length != 3) {
                sender.sendMessage(CC.RED + "Usage: /permission remove <player> <permission>");
                return;
            }

            Profile profile = core.getProfileHandler().getProfile(args[1]);

            if (profile == null) {
                sender.sendMessage(kCoreConstant.dataNotFound(args[1]));
                return;
            }

            String permission = args[1].toLowerCase();

            if (!profile.getPermissions().contains(permission)) {
                sender.sendMessage(CC.RED + profile.getName() + " does not have permission " + permission + '.');
                return;
            }

            profile.getPermissions().remove(permission);
            profile.save();
            new ProfileUpdatePacket(profile, ProfileUpdatePacketType.CHECK_GRANTS).send();

            sender.sendMessage(CC.YELLOW + "Removed permission " + CC.WHITE + permission + CC.YELLOW + " from " + profile.getColoredName() + CC.YELLOW + '.');
            return;
        }

        printHelp(sender);
    }

    private void printHelp(CommandSender sender) {
        sender.sendMessage(new String[]{
                kCoreConstant.CHAT_LINE,
                CC.GOLD + "Permissions",
                kCoreConstant.INDENT + CC.YELLOW + "/permission list <player>" + CC.GRAY + " - " + CC.WHITE + "List owned permissions of a player.",
                kCoreConstant.INDENT + CC.YELLOW + "/permission add <player> <permission>" + CC.GRAY + " - " + CC.WHITE + "Add a permission to a player.",
                kCoreConstant.INDENT + CC.YELLOW + "/permission remove <player> <permission>" + CC.GRAY + " - " + CC.WHITE + "Remove a permission from a player.",
                kCoreConstant.CHAT_LINE
        });
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("list", "add", "remove");
        }

        if (args.length == 2) {
            return getPlayerNames();
        }

        if (args.length == 3) {
            Profile profile = core.getProfileHandler().getProfile(args[1]);
            if (profile != null) {
                return new ArrayList<>(profile.getPermissions());
            }
        }

        return Collections.emptyList();
    }

}