package me.vifez.core.util.menu.paged;

import me.vifez.core.util.menu.Button;
import me.vifez.core.util.menu.Menu;
import me.vifez.core.util.menu.paged.buttons.PageButton;
import me.vifez.core.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class PagedMenu extends Menu {

    private int page = 1;
    private int entriesPerPage = 9;

    @Override
    public void open(Player player) {
        String title = CC.translate(getTitle(player).substring(0, Math.min(getTitle(player).length(), 32)));
        buttons = getButtons(player);

        Menu previous = openedMenus.get(player);
        int size = this.size == -1 ? calculateSize(buttons) : this.size;

        Inventory inventory = null;
        boolean update = false;

        if (player.getOpenInventory() != null) {
            if (previous == null) {
                player.closeInventory();
            } else {
                openedMenus.remove(player);
                Inventory openInventory = player.getOpenInventory().getTopInventory();
                int previousSize = openInventory.getSize();

                if (previousSize == size && openInventory.getTitle().equals(title)) {
                    inventory = openInventory;
                    update = true;
                } else {
                    player.closeInventory();
                }
            }
        }

        if (inventory == null) {
            inventory = Bukkit.createInventory(player, size, title);
        }

        inventory.setContents(new ItemStack[size]);

        for (Map.Entry<Integer, Button> buttonEntry : buttons.entrySet()) {
            inventory.setItem(buttonEntry.getKey(), buttonEntry.getValue().getButtonItem(player));
        }

        if (update) {
            player.updateInventory();
        } else {
            player.closeInventory();
        }

        openedMenus.put(player, this);
        player.openInventory(inventory);
    }

    @Override
    public final Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int min = (int) ((double) (page - 1) * entriesPerPage);
        int max = (int) ((double) (page) * entriesPerPage);
        int top = 0;

        for (Map.Entry<Integer, Button> buttonEntry : getAllButtons(player).entrySet()) {
            int i = buttonEntry.getKey();
            if (i >= min && i < max) {
                i -= (int) ((double) (entriesPerPage) * (page - 1)) - 9;
                buttons.put(i, buttonEntry.getValue());

                if (i > top) {
                    top = i;
                }
            }
        }

        buttons.put(0, new PageButton(-1, this));
        buttons.put(8, new PageButton(1, this));

        for (int i = 1; i < 8; i++) {
            buttons.put(i, getPlaceholder());
        }

        Map<Integer, Button> global = getGlobalButtons(player);

        if (global != null) {
            for (Map.Entry<Integer, Button> buttonEntry : global.entrySet()) {
                buttons.put(buttonEntry.getKey(), buttonEntry.getValue());
            }
        }

        return buttons;
    }

    public final void modPage(Player player, int mod) {
        page += mod;
        getButtons().clear();
        open(player);
    }

    public final int getMaxPages(Player player) {
        int buttons = getAllButtons(player).size();

        if (buttons == 0) {
            return 1;
        }

        return (int) Math.ceil(buttons / (double) entriesPerPage);
    }

    public abstract Map<Integer, Button> getAllButtons(Player player);

    public Map<Integer, Button> getGlobalButtons(Player player) {
        return null;
    }

    public int getEntriesPerPage() {
        return entriesPerPage;
    }

    public void setEntriesPerPage(int entriesPerPage) {
        this.entriesPerPage = entriesPerPage;
    }

    public int getPage() {
        return page;
    }

}