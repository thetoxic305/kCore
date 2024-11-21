package me.vifez.core.util.menu.paged.filterable.buttons;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.menu.Button;
import me.vifez.core.util.menu.paged.filterable.FilterablePagedMenu;
import me.vifez.core.util.menu.paged.filterable.PageFilter;
import me.vifez.core.util.CC;
import me.vifez.core.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PageFilterButton<T> extends Button {

    private FilterablePagedMenu<T> menu;

    public PageFilterButton(FilterablePagedMenu<T> menu) {
        this.menu = menu;

        menu.setUpdate(true);
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        if (menu.getFilters().isEmpty()) {
            return new ItemStack(Material.AIR);
        }

        List<String> lore = new ArrayList<>();

        for (PageFilter<T> filter : menu.getFilters()) {
            String color = CC.RED;

            if (menu.getFilters().get(menu.getScroll()).equals(filter) && filter.isEnabled()) {
                color = CC.GREEN;
            }

            lore.add(color + filter.getName());
        }

        lore.add(kCoreConstant.CHAT_LINE);
        lore.add(CC.GOLD + "Left-click &eto scroll and");
        lore.add(CC.YELLOW + " select a filter");
        lore.add(kCoreConstant.CHAT_LINE);

        return new ItemBuilder(Material.HOPPER)
                .setName(CC.GOLD + "Filters")
                .setLore(lore)
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (!clickType.isLeftClick()) {
            return;
        }

        if (menu.getFilters().isEmpty()) {
            player.sendMessage(CC.RED + "No filters found.");
            return;
        }

        for (PageFilter<T> filter : menu.getFilters()) {
            filter.setEnabled(false);
        }

        if (menu.getScroll() == menu.getFilters().size() - 1) {
            menu.setScroll(0);
        } else {
            menu.setScroll(menu.getScroll() + 1);
        }

        PageFilter<T> filter = menu.getFilters().get(menu.getScroll());
        filter.setEnabled(!filter.isEnabled());
    }

}