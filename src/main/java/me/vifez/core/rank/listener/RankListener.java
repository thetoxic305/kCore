package me.vifez.core.rank.listener;

import me.vifez.core.kCore;
import me.vifez.core.rank.Rank;
import me.vifez.core.rank.command.RankCommand;
import me.vifez.core.util.CC;
import me.vifez.core.util.Pair;
import me.vifez.core.rank.packets.RankPermissionPacket;
import me.vifez.core.rank.packets.RankReloadPacket;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RankListener implements Listener {

    private final kCore core;
    private Set<UUID> add = new HashSet<>();

    public RankListener(kCore core) {
        this.core = core;
    }

    @EventHandler
    private void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Pair<UUID, String> pair = core.getRankHandler().getEditingProperties().get(player.getUniqueId());

        if (pair == null) return;
        event.setCancelled(true);

        String value = event.getMessage();
        Rank rank = core.getRankHandler().getRank(pair.getElementOne());
        String property = pair.getElementTwo().toLowerCase();

        if (add.contains(player.getUniqueId())) {
            if (property.equals("inherit")) {
                Rank inherit = core.getRankHandler().getRank(value);

                if (inherit == null) {
                    player.sendMessage(CC.translate(core.getMessage("rank.not.exist").replace("{rank}", value)));
                    return;
                }

                if (rank.equals(inherit)) {
                    player.sendMessage(CC.translate(core.getMessage("rank.cannot.inherit.itself")));
                    return;
                }

                if (rank.getInherits().contains(inherit)) {
                    player.sendMessage(CC.translate(core.getMessage("rank.already.inherits")
                            .replace("{rank}", rank.getName())
                            .replace("{inherit}", inherit.getName())));
                    return;
                }

                rank.getInherits().add(inherit);
                player.sendMessage(CC.translate(core.getMessage("rank.added.inherit")
                        .replace("{inherit}", inherit.getColoredName())
                        .replace("{rank}", rank.getColoredName())));
            }

            if (property.equals("permission")) {
                String permission = value.toLowerCase();

                if (rank.getPermissions().contains(permission)) {
                    player.sendMessage(CC.translate(core.getMessage("rank.already.has.permission")
                            .replace("{rank}", rank.getName())
                            .replace("{permission}", permission)));
                    return;
                }

                rank.getPermissions().add(permission);
                player.sendMessage(CC.translate(core.getMessage("rank.added.permission")
                        .replace("{permission}", permission)
                        .replace("{rank}", rank.getColoredName())));
            }

            rank.save();
            new RankReloadPacket(rank).send();
            new RankPermissionPacket().send();

            add.remove(player.getUniqueId());
            core.getRankHandler().getEditingProperties().remove(player.getUniqueId());
            return;
        }

        switch (property) {

            case "name": {
                rank = core.getRankHandler().getRank(value);
                if (rank != null) {
                    player.sendMessage(CC.translate(core.getMessage("rank.name.exists").replace("{name}", rank.getName())));
                    return;
                }

                rank = core.getRankHandler().getRank(pair.getElementOne());
                player.sendMessage(CC.translate(core.getMessage("rank.set.name")
                        .replace("{oldName}", rank.getColoredName())
                        .replace("{newName}", value)));
                rank.setName(value);
                rank.save();
                new RankReloadPacket(rank).send();
                break;
            }

            case "priority": {
                if (!StringUtils.isNumeric(value)) {
                    player.sendMessage(CC.translate(core.getMessage("rank.invalid.priority")));
                    return;
                }

                int priority = Integer.parseInt(value);
                rank.setPriority(priority);
                player.sendMessage(CC.translate(core.getMessage("rank.set.priority")
                        .replace("{rank}", rank.getColoredName())
                        .replace("{priority}", String.valueOf(priority))));
                rank.save();
                new RankReloadPacket(rank).send();
                break;
            }

            case "prefix": {
                if (rank.getRawPrefix().equals(value)) {
                    player.sendMessage(CC.translate(core.getMessage("rank.already.has.prefix")
                            .replace("{rank}", rank.getName())
                            .replace("{prefix}", rank.getDisplayPrefix())));
                    break;
                }

                if (value.equals("&f")) {
                    value = "";
                }

                rank.setPrefix(value);
                player.sendMessage(CC.translate(core.getMessage("rank.set.prefix")
                        .replace("{rank}", rank.getColoredName())
                        .replace("{prefix}", rank.getDisplayPrefix())));
                rank.save();
                new RankReloadPacket(rank).send();
                break;
            }

            case "suffix": {
                if (rank.getRawSuffix().equals(value)) {
                    player.sendMessage(CC.translate(core.getMessage("rank.already.has.suffix")
                            .replace("{rank}", rank.getName())
                            .replace("{suffix}", rank.getDisplaySuffix())));
                    break;
                }

                if (value.equals("&f")) {
                    value = "";
                }

                rank.setSuffix(value);
                player.sendMessage(CC.translate(core.getMessage("rank.set.suffix")
                        .replace("{rank}", rank.getColoredName())
                        .replace("{suffix}", rank.getDisplaySuffix())));
                rank.save();
                new RankReloadPacket(rank).send();
                break;
            }

            case "permission": {
                if (!value.equalsIgnoreCase("add") && !value.equalsIgnoreCase("remove")) {
                    player.sendMessage(CC.translate(core.getMessage("rank.permission.add.remove.not.specified")));
                    break;
                }

                boolean add = value.equalsIgnoreCase("add");

                if (add) {
                    this.add.add(player.getUniqueId());
                    player.sendMessage(CC.translate(core.getMessage("rank.enter.permission").replace("{rank}", rank.getColoredName())));
                    return;
                }

                if (rank.getPermissions().isEmpty()) {
                    player.sendMessage(CC.translate(core.getMessage("rank.no.permissions").replace("{rank}", rank.getName())));
                    break;
                }

                player.sendMessage(new String[]{
                        core.getMessage("chat.line"),
                        rank.getColoredName() + CC.GRAY + " - " + CC.WHITE + core.getMessage("rank.permissions.header")
                });

                for (String permission : rank.getPermissions()) {
                    TextComponent text = new TextComponent(CC.WHITE + permission);
                    TextComponent remove = new TextComponent(CC.RED + " [Remove]");
                    remove.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate(core.getMessage("rank.remove.permission.hover")
                            .replace("{permission}", permission)
                            .replace("{rank}", rank.getColoredName()))).create()));
                    remove.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rank " + RankCommand.REMOVE_ID.toString() + " permission " + rank.getName() + ' ' + permission));
                    text.addExtra(remove);
                    player.spigot().sendMessage(text);
                }

                player.sendMessage(core.getMessage("chat.line"));
                break;
            }

            case "inherit": {
                if (!value.equalsIgnoreCase("add") && !value.equalsIgnoreCase("remove")) {
                    player.sendMessage(CC.translate(core.getMessage("rank.inherit.add.remove.not.specified")));
                    break;
                }

                boolean add = value.equalsIgnoreCase("add");

                if (add) {
                    this.add.add(player.getUniqueId());
                    player.sendMessage(CC.translate(core.getMessage("rank.enter.inherit").replace("{rank}", rank.getColoredName())));
                    return;
                }

                if (rank.getInherits().isEmpty()) {
                    player.sendMessage(CC.translate(core.getMessage("rank.no.inherits").replace("{rank}", rank.getName())));
                    break;
                }

                player.sendMessage(new String[]{
                        core.getMessage("chat.line"),
                        rank.getColoredName() + CC.GRAY + " - " + CC.WHITE + core.getMessage("rank.inherits.header")
                });

                for (Rank inherit : rank.getInherits()) {
                    TextComponent text = new TextComponent(inherit.getColoredName());
                    TextComponent remove = new TextComponent(CC.RED + " [Remove]");
                    remove.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate(core.getMessage("rank.remove.inherit.hover")
                            .replace("{inherit}", inherit.getColoredName())
                            .replace("{rank}", rank.getColoredName()))).create()));
                    remove.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rank " + RankCommand.REMOVE_ID.toString() + " inherit " + rank.getName() + ' ' + inherit.getName()));
                    text.addExtra(remove);
                    player.spigot().sendMessage(text);
                }

                player.sendMessage(core.getMessage("chat.line"));
                break;
            }
        }

        core.getRankHandler().getEditingProperties().remove(player.getUniqueId());
    }
}