package me.vifez.core.util.menu.local;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public abstract class LocalInventoryButton {

    private static Set<LocalInventoryButton> localButtons = new HashSet<>();

    public static void registerLocalButton(LocalInventoryButton button) {
        localButtons.add(button);
    }

    public static Set<LocalInventoryButton> getLocalButtons() {
        return localButtons;
    }

    public abstract ItemStack getButtonItem(Player player);

    public void rightClicked(Player player) {
    }

    public void rightClicked(Player player, Entity entity) {
    }

    public boolean canUse(Player player) {
        return true;
    }

}