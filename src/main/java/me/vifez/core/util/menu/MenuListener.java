package me.vifez.core.util.menu;

import me.vifez.core.util.menu.local.LocalInventoryButton;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class MenuListener implements Listener {

    @EventHandler
    private void onItemInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack item = event.getItem();

        if (item == null) {
            return;
        }

        for (LocalInventoryButton button : LocalInventoryButton.getLocalButtons()) {
            if (button.getButtonItem(player).equals(item) && button.canUse(player)) {
                event.setCancelled(true);
                button.rightClicked(player);
                break;
            }
        }
    }

    @EventHandler
    private void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        ItemStack item = player.getItemInHand();

        if (item == null) {
            return;
        }

        for (LocalInventoryButton button : LocalInventoryButton.getLocalButtons()) {
            if (button.getButtonItem(player).equals(item) && button.canUse(player)) {
                event.setCancelled(true);
                button.rightClicked(player, event.getRightClicked());
                break;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Menu menu = Menu.getOpenedMenus().get(player);

        if (menu == null) {
            return;
        }

        event.setCancelled(true);

        if (menu.getButtons().containsKey(event.getSlot())) {
            Button button = menu.getButtons().get(event.getSlot());
            button.clicked(player, event.getClick());

            if (menu.shouldUpdate()) {
                Menu newMenu = Menu.getOpenedMenus().get(player);

                if (newMenu == null) {
                    return;
                }

                for (Map.Entry<Integer, Button> buttonEntry : newMenu.getButtons(player).entrySet()) {
                    player.getOpenInventory().getTopInventory().setItem(buttonEntry.getKey(), buttonEntry.getValue().getButtonItem(player));
                }

                player.updateInventory();
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Menu menu = Menu.getOpenedMenus().get(player);

        if (menu == null) {
            return;
        }

        Menu.getOpenedMenus().remove(player);
    }

}