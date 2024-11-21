package me.vifez.core.staff.menu;

import me.vifez.core.util.menu.Button;
import me.vifez.core.util.menu.Menu;
import me.vifez.core.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class InspectorMenu extends Menu {

    private Player inspecting;

    public InspectorMenu(Player inspecting) {
        this.inspecting = inspecting;

        setSize(45);
    }

    @Override
    public String getTitle(Player player) {
        return inspecting.getDisplayName() + " &7- &fInspecting";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (int i = 0; i < 4; i++) {
            ItemStack item = inspecting.getInventory().getArmorContents()[i];
            buttons.put(i, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return item == null ? new ItemStack(Material.AIR) : null;
                }
            });
        }

        for (int i = 9; i < player.getInventory().getSize() + 9; i++) {
            ItemStack item = inspecting.getInventory().getContents()[i - 9];
            buttons.put(i, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return item == null ? new ItemStack(Material.AIR) : item;
                }
            });
        }

        buttons.put(44, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.PAPER)
                        .setName("&6Information")
                        .setLore(
                                "&eHealth&7: &f" + player.getHealth() + '/' + player.getMaxHealth(),
                                "&eFood&7: &f" + player.getFoodLevel() + "/20"
                        ).build();
            }
        });

        return buttons;
    }

}