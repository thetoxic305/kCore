package me.vifez.core.staff.listener;

import me.vifez.core.kCore;
import me.vifez.core.profile.Profile;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class StaffListener implements Listener {

    private final kCore core;

    public StaffListener(kCore core) {
        this.core = core;
    }

    @EventHandler
    private void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) event.getDamager();
        Profile profile = core.getProfileHandler().getProfile(player.getUniqueId());

        if (profile.getStaffOptions().isVanished() || profile.getStaffOptions().isStaffMode() || profile.getProfileOptions().isFrozen()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) event.getEntity();
        Profile profile = core.getProfileHandler().getProfile(player.getUniqueId());

        if (profile.getStaffOptions().isVanished() || profile.getStaffOptions().isStaffMode() || profile.getProfileOptions().isFrozen()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onInventoryInteract(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        Profile profile = core.getProfileHandler().getProfile(player.getUniqueId());
        if (!profile.getStaffOptions().isStaffMode() && !profile.getProfileOptions().isFrozen()) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    private void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        Profile profile = core.getProfileHandler().getProfile(player.getUniqueId());
        if (!profile.getStaffOptions().isStaffMode() && !profile.getProfileOptions().isFrozen() && !profile.getStaffOptions().isVanished()) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    private void onItemPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();

        Profile profile = core.getProfileHandler().getProfile(player.getUniqueId());
        if (!profile.getStaffOptions().isStaffMode() && !profile.getProfileOptions().isFrozen() && !profile.getStaffOptions().isVanished()) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        Profile profile = core.getProfileHandler().getProfile(player.getUniqueId());
        if (!profile.getStaffOptions().isStaffMode() && !profile.getProfileOptions().isFrozen() && !profile.getStaffOptions().isVanished()) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission("core.staff")) {
            return;
        }

        Profile profile = core.getProfileHandler().getProfile(player.getUniqueId());

        if (!profile.getStaffOptions().isStaffMode() && !profile.getProfileOptions().isFrozen() && !profile.getStaffOptions().isVanished()) {
            return;
        }

        event.setCancelled(true);
    }

}