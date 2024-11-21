package me.vifez.core.rank.menu;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.menu.Button;
import me.vifez.core.util.menu.Menu;
import me.vifez.core.util.ItemBuilder;
import me.vifez.core.util.WoolColor;
import me.vifez.core.kCore;
import me.vifez.core.rank.Rank;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class RankListMenu extends Menu {

    private final kCore core;

    public RankListMenu(kCore core) {
        this.core = core;
    }

    @Override
    public String getTitle(Player player) {
        return "&bRank List &b(" + core.getRankHandler().getRanks().size() + ')';
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Rank rank : core.getRankHandler().getSortedRanks()) {
            buttons.put(buttons.size(), new Button() {

                @Override
                public ItemStack getButtonItem(Player player) {
                    return new ItemBuilder(new ItemStack(Material.WOOL, 1, WoolColor.fromChatColor(rank.getRawColor())))
                            .setName(rank.getColoredName())
                            .setLore(
                                    kCoreConstant.ITEM_LINE,
                                    kCoreConstant.INDENT + "&bDefault&7: " + rank.getDefaultDisplay(),
                                    kCoreConstant.INDENT + "&bPriority&7: &f" + rank.getPriority(),
                                    kCoreConstant.INDENT + "&bColor&7: &f" + rank.getDisplayColor(),
                                    kCoreConstant.INDENT + "&bPrefix&7: &f" + rank.getDisplayPrefix(),
                                    kCoreConstant.INDENT + "&bSuffix&7: &f" + rank.getDisplaySuffix(),
                                    kCoreConstant.INDENT + "&bPermissions&7: &f" + rank.getPermissions().size(),
                                    kCoreConstant.INDENT + "&bInherits&7: &f" + rank.getInherits().size(),
                                    kCoreConstant.ITEM_LINE,
                                    "&bLeft-click &fto edit this rank.",
                                    kCoreConstant.ITEM_LINE
                            )
                            .build();
                }

                @Override
                public void clicked(Player player, ClickType clickType) {

                    if (clickType.isLeftClick()) {
                        player.closeInventory();
                        new RankEditMenu(core, rank).open(player);
                    }

                }

            });
        }

        return buttons;
    }

}