package me.vifez.core.grant.commands;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.util.PlayerUtil;
import me.vifez.core.util.TimeUtil;
import me.vifez.core.kCore;
import me.vifez.core.grant.Grant;
import me.vifez.core.grant.menu.GrantRankListMenu;
import me.vifez.core.profile.Profile;
import me.vifez.core.profile.packets.ProfileUpdatePacket;
import me.vifez.core.profile.packets.ProfileUpdatePacketType;
import me.vifez.core.rank.Rank;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class GrantCommand extends Command {

    private final kCore core;

    public GrantCommand(kCore core) {
        super("grant");

        this.core = core;

        setPermission("glacial.owner");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1 && PlayerUtil.isPlayer(sender)) {
            Profile profile = core.getProfileHandler().getProfile(args[0]);

            if (profile == null) {
                sender.sendMessage(kCoreConstant.dataNotFound(args[0]));
                return;
            }
            new GrantRankListMenu(profile, core).open(PlayerUtil.getPlayer(sender));
            return;
        }

        if (args.length >= 4 && !PlayerUtil.isPlayer(sender)) {
            Profile profile = core.getProfileHandler().getProfile(args[0]);

            if (profile == null) {
                sender.sendMessage(kCoreConstant.dataNotFound(args[0]));
                return;
            }

            Rank rank = core.getRankHandler().getRank(args[1]);
            if (rank == null) {
                sender.sendMessage(CC.RED + "No rank with name " + args[1] + " found.");
                return;
            }

            long duration = TimeUtil.parseTime(args[2]);

            if (args[2].equalsIgnoreCase("permanent") || args[2].equalsIgnoreCase("perm")) {
                duration = Long.MAX_VALUE;
            }

            if (duration == -1L) {
                sender.sendMessage(CC.RED + "Invalid duration provided.");
                return;
            }

            String reason = StringUtils.join(args, " ", 3, args.length);

            if (reason.equalsIgnoreCase("Default Grant")) {
                sender.sendMessage(CC.RED + "Invalid reason.");
                return;
            }

            Grant grant = new Grant(rank, duration, System.currentTimeMillis(), reason, null);
            profile.getGrants().add(grant);
            profile.save();
            profile.checkGrants();
            profile.setupPlayer();
            new ProfileUpdatePacket(profile, ProfileUpdatePacketType.CHECK_GRANTS).send();

            sender.sendMessage(new String[]{
                    kCoreConstant.CHAT_LINE,
                    CC.GOLD + "Grant Applied",
                    kCoreConstant.INDENT + CC.YELLOW + "Player" + CC.GRAY + ": " + CC.WHITE + profile.getColoredName(),
                    kCoreConstant.INDENT + CC.YELLOW + "Executor" + CC.GRAY + ": " + CC.DARK_RED + "Console",
                    kCoreConstant.INDENT + CC.YELLOW + "Reason" + CC.GRAY + ": " + CC.WHITE + reason,
                    kCoreConstant.INDENT + CC.YELLOW + "Duration" + CC.GRAY + ": " + CC.WHITE + grant.getDurationText(),
                    kCoreConstant.CHAT_LINE
            });
            return;
        }

        sender.sendMessage(CC.RED + "Usage: /grant <player>");
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return getPlayerNames();
        }
        return Collections.emptyList();
    }

}