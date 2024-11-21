package me.vifez.core.staff;

import me.vifez.core.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StaffOptions {

    private final Profile profile;

    public StaffOptions(Profile profile) {
        this.profile = profile;
    }

    private boolean staffChat = false;
    private boolean vanished = false;
    private boolean staffMode = false;

    private ItemStack[] inventoryCache;
    private ItemStack[] armorCache;

    public boolean isStaffChat() {
        return staffChat;
    }

    public void setStaffChat(boolean staffChat) {
        this.staffChat = staffChat;
    }

    public boolean isVanished() {
        return vanished;
    }

    public void setVanished(boolean vanished) {
        Player player = profile.getPlayer();
        if (player == null) {
            return;
        }

        this.vanished = vanished;

        if (vanished) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.hasPermission("core.staff"))
                    continue;

                onlinePlayer.hidePlayer(player);
            }
            return;
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.showPlayer(player);
        }
    }

    public boolean isStaffMode() {
        return staffMode;
    }

    public void setStaffMode(boolean staffMode) {
        Player player = profile.getPlayer();
        if (player == null) {
            return;
        }

        this.staffMode = staffMode;

        if (staffMode) {
            player.setGameMode(GameMode.CREATIVE);
            setVanished(true);

            inventoryCache = player.getInventory().getContents();
            armorCache = player.getInventory().getArmorContents();

            player.getInventory().clear();

            player.getInventory().setItem(0, StaffMode.COMPASS.getItem(player));
            player.getInventory().setItem(1, StaffMode.INSPECTOR.getItem(player));
            player.getInventory().setItem(2, StaffMode.FREEZE.getItem(player));

            player.getInventory().setItem(6, StaffMode.RANDOM_TELEPORT.getItem(player));
            player.getInventory().setItem(7, staffChat ? StaffMode.STAFFCHAT_ENABLED.getItem(player) : StaffMode.STAFFCHAT_DISABLED.getItem(player));
            player.getInventory().setItem(8, StaffMode.VANISH_ENABLED.getItem(player));

        } else {
            player.setGameMode(GameMode.SURVIVAL);
            setVanished(false);

            player.getInventory().setContents(inventoryCache);
            player.getInventory().setArmorContents(armorCache);

            inventoryCache = null;
            armorCache = null;
        }

        player.updateInventory();
    }

    public ItemStack[] getInventoryCache() {
        return inventoryCache;
    }

    public void setInventoryCache(ItemStack[] inventoryCache) {
        this.inventoryCache = inventoryCache;
    }

    public ItemStack[] getArmorCache() {
        return armorCache;
    }

    public void setArmorCache(ItemStack[] armorCache) {
        this.armorCache = armorCache;
    }

}