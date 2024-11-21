package me.vifez.core.rank.menu;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.menu.Button;
import me.vifez.core.util.menu.Menu;
import me.vifez.core.util.CC;
import me.vifez.core.util.ItemBuilder;
import me.vifez.core.util.Pair;
import me.vifez.core.kCore;
import me.vifez.core.rank.Rank;
import me.vifez.core.rank.packets.RankReloadPacket;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class RankEditMenu extends Menu {

    private final kCore core;
    private Rank rank;

    public RankEditMenu(kCore core, Rank rank) {
        this.core = core;
        this.rank = rank;

        setSize(27);
    }

    @Override
    public String getTitle(Player player) {
        return rank.getColoredName() + " &7- &fEdit";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(4, new Button() {

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.NAME_TAG)
                        .setName("&bName")
                        .setLore(
                                kCoreConstant.ITEM_LINE,
                                kCoreConstant.INDENT + "&bValue&7: &f" + rank.getName(),
                                kCoreConstant.ITEM_LINE,
                                "&bLeft-click &fto set the name of this rank.",
                                kCoreConstant.ITEM_LINE
                        )
                        .build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                if (!clickType.isLeftClick()) {
                    return;
                }

                player.closeInventory();
                core.getRankHandler().getEditingProperties().put(player.getUniqueId(), new Pair<>(rank.getUUID(), "name"));
                player.sendMessage(CC.WHITE + "Please enter a new name for " + rank.getColoredName() + CC.WHITE + '.');
            }

        });

        buttons.put(22, new Button() {

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.REDSTONE)
                        .setName("&cClose")
                        .setLore(
                                kCoreConstant.ITEM_LINE,
                                "&bLeft-click &fto close the menu.",
                                kCoreConstant.ITEM_LINE
                        )
                        .build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                if (!clickType.isLeftClick()) {
                    return;
                }

                player.closeInventory();
            }

        });

        buttons.put(10, new Button() {

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.PAPER)
                        .setName("&bDefault")
                        .setLore(
                                kCoreConstant.ITEM_LINE,
                                kCoreConstant.INDENT + "&bValue&7: &f" + rank.getDefaultDisplay(),
                                kCoreConstant.ITEM_LINE,
                                "&bLeft-click &fto toggle this rank as default.",
                                kCoreConstant.ITEM_LINE
                        )
                        .build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                if (!clickType.isLeftClick()) {
                    return;
                }

                player.closeInventory();

                Rank defaultRank = core.getRankHandler().getDefaultRank();

                if (rank.equals(defaultRank)) {
                    player.sendMessage(CC.RED + "You cannot toggle the current default rank.");
                    return;
                }

                defaultRank.setDefault(false);
                rank.setDefault(true);

                defaultRank.save();
                rank.save();

                new RankReloadPacket(defaultRank).send();
                new RankReloadPacket(rank).send();

                player.sendMessage(CC.WHITE + "Set the default rank to " + rank.getColoredName() + CC.WHITE + '.');
            }

        });

        buttons.put(11, new Button() {

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.PAPER)
                        .setName("&bPriority")
                        .setLore(
                                kCoreConstant.ITEM_LINE,
                                kCoreConstant.INDENT + "&bValue&7: &f" + rank.getPriority(),
                                kCoreConstant.ITEM_LINE,
                                "&bLeft-click &fto set the priority of this rank.",
                                kCoreConstant.ITEM_LINE
                        )
                        .build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                if (!clickType.isLeftClick()) {
                    return;
                }

                player.closeInventory();
                core.getRankHandler().getEditingProperties().put(player.getUniqueId(), new Pair<>(rank.getUUID(), "priority"));
                player.sendMessage(CC.WHITE + "Please enter a priority for " + rank.getColoredName() + CC.WHITE + '.');
            }

        });

        buttons.put(12, new Button() {

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.PAPER)
                        .setName("&bColor")
                        .setLore(
                                kCoreConstant.ITEM_LINE,
                                kCoreConstant.INDENT + "&bValue&7: &f" + rank.getDisplayColor(),
                                kCoreConstant.ITEM_LINE,
                                "&bLeft-click &fto set the color of this rank.",
                                kCoreConstant.ITEM_LINE
                        )
                        .build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                if (!clickType.isLeftClick()) {
                    return;
                }

                player.closeInventory();
                new RankColorMenu(rank).open(player);
            }

        });

        buttons.put(13, new Button() {

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.PAPER)
                        .setName("&bPrefix")
                        .setLore(
                                kCoreConstant.ITEM_LINE,
                                kCoreConstant.INDENT + "&bValue&7: &f" + rank.getDisplayPrefix(),
                                kCoreConstant.ITEM_LINE,
                                "&bLeft-click &fto set the prefix of this rank.",
                                kCoreConstant.ITEM_LINE
                        )
                        .build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                if (!clickType.isLeftClick()) {
                    return;
                }

                player.closeInventory();
                core.getRankHandler().getEditingProperties().put(player.getUniqueId(), new Pair<>(rank.getUUID(), "prefix"));
                player.sendMessage(CC.WHITE + "Please enter a prefix for " + rank.getColoredName() + CC.WHITE + '.');
            }

        });

        buttons.put(14, new Button() {

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.PAPER)
                        .setName("&bSuffix")
                        .setLore(
                                kCoreConstant.ITEM_LINE,
                                kCoreConstant.INDENT + "&bValue&7: &f" + rank.getDisplaySuffix(),
                                kCoreConstant.ITEM_LINE,
                                "&bLeft-click &fto set the suffix of this rank.",
                                kCoreConstant.ITEM_LINE
                        )
                        .build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                if (!clickType.isLeftClick()) {
                    return;
                }

                player.closeInventory();
                core.getRankHandler().getEditingProperties().put(player.getUniqueId(), new Pair<>(rank.getUUID(), "suffix"));
                player.sendMessage(CC.WHITE + "Please enter a suffix for " + rank.getColoredName() + CC.WHITE + '.');
            }

        });

        buttons.put(15, new Button() {

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.PAPER)
                        .setName("&bPermissions")
                        .setLore(
                                kCoreConstant.ITEM_LINE,
                                kCoreConstant.INDENT + "&bAmount&7: &f" + rank.getPermissions().size(),
                                kCoreConstant.ITEM_LINE,
                                "&bLeft-click &fto edit the permissions of this rank.",
                                kCoreConstant.ITEM_LINE
                        )
                        .build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                if (!clickType.isLeftClick()) {
                    return;
                }

                player.closeInventory();
                core.getRankHandler().getEditingProperties().put(player.getUniqueId(), new Pair<>(rank.getUUID(), "permission"));
                player.sendMessage(CC.WHITE + "Please specify to " + CC.GREEN + "add" + CC.WHITE + " or " + CC.RED + "remove" + CC.WHITE + " a permission.");
            }

        });

        buttons.put(16, new Button() {

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.PAPER)
                        .setName("&bInherits")
                        .setLore(
                                kCoreConstant.ITEM_LINE,
                                kCoreConstant.INDENT + "&bAmount&7: &f" + rank.getInherits().size(),
                                kCoreConstant.ITEM_LINE,
                                "&bLeft-click &fto edit the inherits of this rank.",
                                kCoreConstant.ITEM_LINE
                        )
                        .build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                if (!clickType.isLeftClick()) {
                    return;
                }

                player.closeInventory();
                core.getRankHandler().getEditingProperties().put(player.getUniqueId(), new Pair<>(rank.getUUID(), "inherit"));
                player.sendMessage(CC.WHITE + "Please specify to " + CC.GREEN + "add" + CC.WHITE + " or " + CC.RED + "remove" + CC.WHITE + " an inherit.");
            }

        });

        return buttons;
    }

}