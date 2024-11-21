package me.vifez.core.util.menu.paged.filterable;

import me.vifez.core.util.menu.Button;
import me.vifez.core.util.menu.paged.PagedMenu;
import me.vifez.core.util.menu.paged.filterable.buttons.PageFilterButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FilterablePagedMenu<T> extends PagedMenu {

    private List<PageFilter<T>> filters;
    private int scroll = 0;

    {
        filters = generateFilters();
        for (PageFilter<T> filter : filters) {
            if (filter.getName().equalsIgnoreCase("All")) {
                filter.setEnabled(true);
            }
        }
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(7, new PageFilterButton<>(this));
        return buttons;
    }

    public abstract List<PageFilter<T>> generateFilters();

    public List<PageFilter<T>> getFilters() {
        return filters;
    }

    public int getScroll() {
        return scroll;
    }

    public void setScroll(int scroll) {
        this.scroll = scroll;
    }

}