package me.vifez.core.rank.command;

import me.vifez.core.kCoreConstant;
import me.vifez.core.rank.Rank;
import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.util.PlayerUtil;
import me.vifez.core.kCore;
import me.vifez.core.rank.menu.RankEditMenu;
import me.vifez.core.rank.menu.RankListMenu;
import me.vifez.core.rank.packets.RankCreatePacket;
import me.vifez.core.rank.packets.RankDeletePacket;
import me.vifez.core.rank.packets.RankPermissionPacket;
import me.vifez.core.rank.packets.RankReloadPacket;
import org.bukkit.command.CommandSender;

import java.util.*;

public class RankCommand extends Command {

    private final kCore core;

    public static UUID REMOVE_ID = UUID.randomUUID();

    public RankCommand(kCore core) {
        super("rank");
        this.core = core;
        setPermission("core.rank");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            printHelp(sender);
            return;
        }

        if (args[0].equalsIgnoreCase("list") && PlayerUtil.isPlayer(sender)) {
            new RankListMenu(core).open(PlayerUtil.getPlayer(sender));
            return;
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (args.length != 2) {
                sender.sendMessage(CC.translate(core.getMessage("rank.usage.create")));
                return;
            }

            Rank rank = core.getRankHandler().getRank(args[1]);
            if (rank != null) {
                sender.sendMessage(CC.translate(core.getMessage("rank.exists").replace("{name}", CC.translate(args[1]))));
                return;
            }

            rank = new Rank(args[1], UUID.randomUUID());
            rank.save();
            new RankCreatePacket(rank).send();
            sender.sendMessage(CC.translate(core.getMessage("rank.created").replace("{name}", rank.getColoredName())));
            return;
        }

        if (args[0].equalsIgnoreCase(REMOVE_ID.toString())) {
            Rank rank = core.getRankHandler().getRank(args[2]);
            if (rank == null) {
                return;
            }
        }

        if (args[0].equalsIgnoreCase("delete")) {
            if (args.length != 2) {
                sender.sendMessage(CC.translate(core.getMessage("rank.usage.delete")));
                return;
            }

            Rank rank = core.getRankHandler().getRank(args[1]);
            if (rank == null) {
                sender.sendMessage(CC.translate(rankNotFound(args[1])));
                return;
            }

            if (rank.isDefault()) {
                sender.sendMessage(CC.translate(core.getMessage("rank.cannot_delete_default")));
                return;
            }

            rank.delete(true);
            new RankDeletePacket(rank).send();
            sender.sendMessage(CC.translate(core.getMessage("rank.deleted").replace("{name}", rank.getColoredName())));
            return;
        }

        if (args[0].equals("edit") && PlayerUtil.isPlayer(sender)) {
            if (args.length != 2) {
                sender.sendMessage(CC.translate(core.getMessage("rank.usage.edit")));
                return;
            }

            Rank rank = core.getRankHandler().getRank(args[1]);
            if (rank == null) {
                sender.sendMessage(CC.translate(rankNotFound(args[1])));
                return;
            }

            new RankEditMenu(core, rank).open(PlayerUtil.getPlayer(sender));
            return;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            Rank rank = core.getRankHandler().getRank(args[2]);
            if (rank == null) {
                return;
            }

            String parameter = args[1];

            if (parameter.equalsIgnoreCase("inherit")) {
                Rank inherit = core.getRankHandler().getRank(args[3]);

                if (!rank.getInherits().contains(inherit)) {
                    sender.sendMessage(CC.translate(core.getMessage("rank.remove.inherit")
                            .replace("{name}", CC.translate(rank.getName()))
                            .replace("{inherit}", CC.translate(inherit.getName()))));
                    return;
                }

                rank.getInherits().remove(inherit);
                sender.sendMessage(CC.translate(core.getMessage("rank.removed.inherit")
                        .replace("{name}", CC.translate(rank.getColoredName()))
                        .replace("{inherit}", CC.translate(inherit.getColoredName()))));
            }

            if (parameter.equalsIgnoreCase("permission")) {
                String permission = args[3].toLowerCase();

                if (!rank.getPermissions().contains(permission)) {
                    sender.sendMessage(CC.translate(core.getMessage("rank.remove.permission")
                            .replace("{name}", CC.translate(rank.getName()))
                            .replace("{permission}", permission)));
                    return;
                }

                rank.getPermissions().remove(permission);
                sender.sendMessage(CC.translate(core.getMessage("rank.removed.permission")
                        .replace("{name}", CC.translate(rank.getColoredName()))
                        .replace("{permission}", permission)));
            }

            rank.save();
            new RankPermissionPacket().send();
            new RankReloadPacket(rank).send();
            return;
        }

        printHelp(sender);
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("list", "create", "delete", "edit");
        }

        if (args.length == 2 && !args[0].equalsIgnoreCase("list") && !args[0].equalsIgnoreCase("create")) {
            List<String> toReturn = new ArrayList<>();
            for (Rank rank : core.getRankHandler().getSortedRanks()) {
                toReturn.add(rank.getName());
            }
            return toReturn;
        }

        return Collections.emptyList();
    }

    private void printHelp(CommandSender sender) {
        sender.sendMessage(CC.translate(new String[]{
                kCoreConstant.CHAT_LINE,
                CC.AQUA + "Rank Help",
                kCoreConstant.INDENT + "&b/rank list &7- &fShow all ranks.",
                kCoreConstant.INDENT + "&b/rank create <name> &7- &fCreate a rank.",
                kCoreConstant.INDENT + "&b/rank delete <rank> &7- &fDelete a rank.",
                kCoreConstant.INDENT + "&b/rank edit <rank> &7- &fEdit properties of a rank.",
                kCoreConstant.CHAT_LINE
        }));
    }

    private String rankNotFound(String name) {
        return CC.translate(core.getMessage("rank.not_found").replace("{name}", name));
    }
}