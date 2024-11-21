package me.vifez.core.chat.commands;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.command.Command;
import me.vifez.core.util.mongo.async.AsyncResult;
import me.vifez.core.util.CC;
import me.vifez.core.util.PlayerUtil;
import me.vifez.core.kCore;
import me.vifez.core.profile.Profile;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class IgnoreCommand extends Command {

    private final kCore core;

    public IgnoreCommand(kCore core) {
        super("ignore");

        this.core = core;

        setPlayerOnly(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            printHelp(sender);
            return;
        }

        Profile profile = core.getProfileHandler().getProfile(PlayerUtil.getPlayer(sender).getUniqueId());

        if (args[0].equalsIgnoreCase("list")) {
            Set<UUID> ignoredUUIDs = profile.getProfileOptions().getIgnored();
            if (ignoredUUIDs.isEmpty()) {
                sender.sendMessage(CC.RED + "You are not ignoring anyone.");
                return;
            }

            AsyncResult<Set<String>> result = new AsyncResult<>();

            Set<String> ignored = new HashSet<>();
            for (UUID ignoredUUID : ignoredUUIDs) {
                core.getProfileHandler().getProfile(ignoredUUID).getColoredName();

                if (ignored.size() == ignoredUUIDs.size()) {
                    result.complete(ignored);
                }
            }

            result.addCallback(ignoredNames -> {
                sender.sendMessage(new String[]{
                        kCoreConstant.CHAT_LINE,
                        CC.GOLD + "Ignored Players",
                        StringUtils.join(ignoredNames, CC.WHITE + ", "),
                        kCoreConstant.CHAT_LINE
                });
            });
            return;
        }

        if (args[0].equalsIgnoreCase("add")) {
            if (args.length != 2) {
                sender.sendMessage(CC.RED + "Usage: /ignore add <player>");
                return;
            }

            Player player = core.getServer().getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage(kCoreConstant.playerNotOnline(args[1]));
                return;
            }

            if (profile.getProfileOptions().getIgnored().contains(player.getUniqueId())) {
                sender.sendMessage(CC.RED + "You are already ignoring " + player.getName() + '.');
                return;
            }

            if (player.getUniqueId().equals(profile.getUUID())) {
                sender.sendMessage(CC.RED + "You cannot ignore yourself.");
                return;
            }

            profile.getProfileOptions().getIgnored().add(player.getUniqueId());
            profile.save();

            sender.sendMessage(CC.YELLOW + "You are " + CC.GREEN + "now" + CC.YELLOW + " ignoring " + player.getDisplayName() + CC.YELLOW + '.');
            return;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (args.length != 2) {
                sender.sendMessage(CC.RED + "Usage: /ignore remove <player>");
                return;
            }

            Profile target = core.getProfileHandler().getProfile(args[1]);

            if (target == null) {
                sender.sendMessage(kCoreConstant.dataNotFound(args[1]));
                return;
            }

            if (!profile.getProfileOptions().getIgnored().contains(target.getUUID())) {
                sender.sendMessage(CC.RED + "You are not ignoring " + target.getName() + '.');
                return;
            }

            profile.getProfileOptions().getIgnored().remove(target.getUUID());
            profile.save();

            sender.sendMessage(CC.YELLOW + "You are " + CC.RED + "no longer" + CC.YELLOW + " ignoring " + target.getColoredName() + CC.YELLOW + '.');
            return;
        }

        printHelp(sender);
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("list", "add", "remove");
        }

        if (args.length == 2) {
            return getPlayerNames();
        }

        return Collections.emptyList();
    }

    private void printHelp(CommandSender sender) {
        sender.sendMessage(new String[]{
                kCoreConstant.CHAT_LINE,
                CC.GOLD + "Ignore Help",
                kCoreConstant.INDENT + CC.YELLOW + "/ignore list" + CC.GRAY + " - " + CC.WHITE + "View who you have ignored.",
                kCoreConstant.INDENT + CC.YELLOW + "/ignore add <player>" + CC.GRAY + " - " + CC.WHITE + "Ignore a player.",
                kCoreConstant.INDENT + CC.YELLOW + "/ignore remove <player>" + CC.GRAY + " - " + CC.WHITE + "Stop ignoring a player.",
                kCoreConstant.CHAT_LINE
        });
    }

}