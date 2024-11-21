package me.vifez.core.staff;

import me.vifez.core.util.menu.local.LocalInventoryButton;
import me.vifez.core.util.CC;
import me.vifez.core.util.ItemBuilder;
import me.vifez.core.util.PlayerUtil;
import me.vifez.core.kCore;
import me.vifez.core.profile.Profile;
import me.vifez.core.staff.menu.InspectorMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum StaffMode {

    COMPASS(new LocalInventoryButton() {

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.COMPASS)
                    .setName("&6Compass")
                    .setLore(
                            "&fLeft-click &eto jump to the",
                            " &eblock you are looking at.",
                            "",
                            "&fRight-click &eto phase through",
                            " &ethe block you are looking at."
                    ).build();
        }

    }),

    INSPECTOR(new LocalInventoryButton() {

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.CHEST)
                    .setName("&6Inspector")
                    .setLore(
                            "&fRight-click &eon a player",
                            " &eto inspect them."
                    ).build();
        }

        @Override
        public void rightClicked(Player player, Entity entity) {
            if (!PlayerUtil.isPlayer(entity)) {
                return;
            }
            new InspectorMenu((Player) entity).open(player);
        }

        @Override
        public boolean canUse(Player player) {
            return StaffMode.canUse(player);
        }

    }),

    FREEZE(new LocalInventoryButton() {

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.IRON_FENCE)
                    .setName("&6Freeze")
                    .setLore(
                            "&fRight-click &eon a player",
                            " &eto toggle their freeze."
                    ).build();
        }

        @Override
        public void rightClicked(Player player, Entity entity) {
            if (!PlayerUtil.isPlayer(entity)) {
                return;
            }
            Player player1 = (Player) entity;
            player.performCommand("freeze " + player1.getName());
        }

        @Override
        public boolean canUse(Player player) {
            return StaffMode.canUse(player);
        }

    }),

    RANDOM_TELEPORT(new LocalInventoryButton() {

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(new ItemStack(Material.SKULL_ITEM, 1, (byte) 3))
                    .setName("&6Random Teleport")
                    .setLore(
                            "&fRight-click &eto teleport",
                            " &eto a random player."
                    )
                    .build();
        }

        @Override
        public void rightClicked(Player player) {
            if (Bukkit.getOnlinePlayers().size() == 1) {
                player.sendMessage(CC.RED + "Could not find a suitable player to teleport you to.");
                return;
            }

            List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
            players.removeIf(player::equals);

            Player random = (Player) players.toArray()[(int) (System.currentTimeMillis() % players.size())];
            player.teleport(random);
            player.sendMessage(CC.YELLOW + "Randomly teleported you to " + random.getDisplayName() + CC.YELLOW + '.');
        }

        @Override
        public boolean canUse(Player player) {
            return StaffMode.canUse(player);
        }

    }),

    VANISH_DISABLED(new LocalInventoryButton() {

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(new ItemStack(Material.INK_SACK, 1, (byte) 8))
                    .setName("&6Vanish&7: &cDisabled")
                    .setLore(
                            "&fRight-click &eto &aenable &evanish."
                    )
                    .build();
        }

        @Override
        public void rightClicked(Player player) {
            player.performCommand("vanish");
        }

        @Override
        public boolean canUse(Player player) {
            return StaffMode.canUse(player);
        }

    }),

    VANISH_ENABLED(new LocalInventoryButton() {

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(new ItemStack(Material.INK_SACK, 1, (byte) 10))
                    .setName("&6Vanish&7: &aEnabled")
                    .setLore(
                            "&fRight-click &eto &cdisable &evanish."
                    )
                    .build();
        }

        @Override
        public void rightClicked(Player player) {
            player.performCommand("vanish");
        }

        @Override
        public boolean canUse(Player player) {
            return StaffMode.canUse(player);
        }

    }),

    STAFFCHAT_DISABLED(new LocalInventoryButton() {

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.LEVER)
                    .setName("&6Staff Chat&7: &cDisabled")
                    .setLore(
                            "&fRight-click &eto &aenable &estaff chat."
                    )
                    .build();
        }

        @Override
        public void rightClicked(Player player) {
            player.performCommand("staffchat");
        }

        @Override
        public boolean canUse(Player player) {
            return StaffMode.canUse(player);
        }

    }),

    STAFFCHAT_ENABLED(new LocalInventoryButton() {

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.REDSTONE_TORCH_ON)
                    .setName("&6Staff Chat&7: &aEnabled")
                    .setLore(
                            "&fRight-click &eto &cdisable &estaff chat."
                    )
                    .build();
        }

        @Override
        public void rightClicked(Player player) {
            player.performCommand("staffchat");
        }

        @Override
        public boolean canUse(Player player) {
            return StaffMode.canUse(player);
        }

    });

    private static boolean canUse(Player player) {
        if (!player.hasPermission("core.staff")) {
            return false;
        }
        Profile profile = kCore.getInstance().getProfileHandler().getProfile(player.getUniqueId());
        return profile.getStaffOptions().isStaffMode();
    }

    public static void init() {
        List<StaffMode> buttons = Arrays.asList(
                COMPASS,
                INSPECTOR,
                FREEZE,
                RANDOM_TELEPORT,
                VANISH_DISABLED,
                VANISH_ENABLED,
                STAFFCHAT_DISABLED,
                STAFFCHAT_ENABLED
        );
        for (StaffMode button : buttons) {
            LocalInventoryButton.registerLocalButton(button.button);
        }
    }

    private LocalInventoryButton button;

    StaffMode(LocalInventoryButton button) {
        this.button = button;
    }

    public ItemStack getItem(Player player) {
        return button.getButtonItem(player);
    }

}