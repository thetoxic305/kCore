package me.vifez.core.server.commands;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.kCore;
import me.vifez.core.profile.Profile;
import me.vifez.core.rank.Rank;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ListCommand extends Command {

    private final kCore core;

    public ListCommand(kCore core) {
        super("list");

        this.core = core;

        addAliases("online", "players");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        List<String> rankNames = new ArrayList<>();
        for (Rank rank : core.getRankHandler().getSortedRanks()) {
            rankNames.add(rank.getColoredName());
        }

        int online = core.getServer().getOnlinePlayers().size();

        List<String> playerNames = new ArrayList<>();
        for (Profile profile : core.getProfileHandler().getSortedProfiles()) {
            if (!profile.isOnline()) {
                continue;
            }

            if (profile.getStaffOptions().isVanished()) {
                if (sender.hasPermission("core.staff")) {
                    playerNames.add(CC.GRAY + "(Vanished) " + profile.getColoredName());
                } else {
                    online--;
                }
                continue;
            }

            playerNames.add(profile.getColoredName());
        }

        sender.sendMessage(new String[]{
                kCoreConstant.CHAT_LINE,
                CC.GOLD + "Player List",
                StringUtils.join(rankNames, CC.WHITE + ", "),
                CC.YELLOW + "(" + online + "/" + core.getServer().getMaxPlayers() + "): " + CC.WHITE + StringUtils.join(playerNames, CC.WHITE + ", "),
                kCoreConstant.CHAT_LINE
        });
    }

}
