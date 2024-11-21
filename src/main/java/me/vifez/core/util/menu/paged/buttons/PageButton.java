package me.vifez.core.util.menu.paged.buttons;

import me.vifez.core.util.menu.Button;
import me.vifez.core.util.menu.paged.PagedMenu;
import me.vifez.core.util.CC;
import me.vifez.core.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class PageButton extends Button {

    private int mod;
    private PagedMenu menu;

    public PageButton(int mod, PagedMenu menu) {
        this.mod = mod;
        this.menu = menu;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        if (mod > 0) {
            if (hasNext(player)) {
                return new ItemBuilder(Material.REDSTONE_TORCH_ON)
                        .setName(CC.GOLD + "Next Page")
                        .setLore(
                                CC.YELLOW + "Click here to go",
                                CC.YELLOW + "to the next page."
                        )
                        .build();
            } else {
                return new ItemBuilder(Material.LEVER)
                        .setName(CC.GOLD + "Next Page")
                        .setLore(
                                CC.RED + "There is no available",
                                CC.RED + "next page."
                        )
                        .build();
            }
        } else {
            if (hasPrevious()) {
                return new ItemBuilder(Material.REDSTONE_TORCH_ON)
                        .setName(CC.GOLD + "Previous Page")
                        .setLore(
                                CC.YELLOW + "Click here to go",
                                CC.YELLOW + "to the previous page."
                        )
                        .build();
            } else {
                return new ItemBuilder(Material.LEVER)
                        .setName(CC.GOLD + "Previous Page")
                        .setLore(
                                CC.RED + "There is no available",
                                CC.RED + "previous page."
                        )
                        .build();
            }
        }
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (mod > 0) {
            if (hasNext(player)) {
                menu.modPage(player, mod);
                Button.playNeutral(player);
            } else {
                Button.playFail(player);
            }
        } else {
            if (hasPrevious()) {
                menu.modPage(player, mod);
                Button.playNeutral(player);
            } else {
                Button.playFail(player);
            }
        }
    }

    private boolean hasNext(Player player) {
        int pg = menu.getPage() + mod;
        return menu.getMaxPages(player) >= pg;
    }

    private boolean hasPrevious() {
        int pg = menu.getPage() + mod;
        return pg > 0;
    }

}
