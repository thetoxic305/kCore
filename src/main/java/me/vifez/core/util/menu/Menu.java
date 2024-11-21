package me.vifez.core.util.menu;

import me.vifez.core.util.CC;
import me.vifez.core.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class Menu {

    protected static Map<Player, Menu> openedMenus = new HashMap<>();

    protected Map<Integer, Button> buttons;
    protected int size = -1;

    protected boolean update = false;

    protected Button placeholder = new Button() {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.STAINED_GLASS_PANE)
                    .setName(" ")
                    .build();
        }
    };

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

        for (int i = 0; i < size; i++) {
            if (buttons.get(i) != null)
                continue;

            buttons.put(i, placeholder);
            inventory.setItem(i, placeholder.getButtonItem(player));
        }

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

    public abstract String getTitle(Player player);

    public abstract Map<Integer, Button> getButtons(Player player);

    protected int calculateSize(Map<Integer, Button> buttons) {
        return (int) (Math.ceil((buttons.size() + 1) / 9D) * 9D);
    }

    public int getSlot(int x, int y) {
        return (y * 9) + x;
    }

    public static Map<Player, Menu> getOpenedMenus() {
        return openedMenus;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Map<Integer, Button> getButtons() {
        return buttons;
    }

    public Button getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(Button placeholder) {
        this.placeholder = placeholder;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean shouldUpdate() {
        return update;
    }

}