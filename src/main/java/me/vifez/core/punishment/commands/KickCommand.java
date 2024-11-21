package me.vifez.core.punishment.commands;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.util.Pair;
import me.vifez.core.util.PlayerUtil;
import me.vifez.core.kCore;
import me.vifez.core.profile.Profile;
import me.vifez.core.punishment.Punishment;
import me.vifez.core.punishment.PunishmentType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class KickCommand extends Command {

    private final kCore core;

    public KickCommand(kCore core) {
        super("kick");

        this.core = core;

        setPermission("core.kick");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 1) {
            Player player = core.getServer().getPlayer(args[0]);

            if (player == null) {
                sender.sendMessage(kCoreConstant.playerNotOnline(args[0]));
                return;
            }

            Profile profile = core.getProfileHandler().getProfile(player.getUniqueId());

            Player executor = PlayerUtil.getPlayer(sender);

            boolean silent = false;

            for (String arg : args) {
                if (arg.equalsIgnoreCase("-s")) {
                    silent = true;
                    break;
                }
            }

            String reason = StringUtils.join(args, " ", 1, args.length).replaceAll("(?i)-s", "");

            Punishment punishment = new Punishment(
                    PunishmentType.KICK,
                    Long.MAX_VALUE,
                    System.currentTimeMillis(),
                    reason,
                    executor == null ? null : executor.getUniqueId()
            );

            profile.getPunishments().add(punishment);

            profile.save();

            player.kickPlayer(core.getPunishmentHandler().getMessage(punishment));

            Pair<String, String> message = core.getPunishmentHandler().getBroadcastMessage(profile, punishment, executor == null ? null : executor.getDisplayName(), silent);

            TextComponent component = new TextComponent(message.getElementOne());
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(message.getElementTwo()).create()));

            for (Player onlinePlayer : core.getServer().getOnlinePlayers()) {
                if (silent && !onlinePlayer.hasPermission("core.kick")) {
                    continue;
                }

                onlinePlayer.spigot().sendMessage(component);
            }
            return;
        }

        sender.sendMessage(CC.RED + "Usage: /kick <player> <reason...> [-s]");
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return getPlayerNames();
        }

        return Collections.emptyList();
    }

}