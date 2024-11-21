package me.vifez.core.rank.menu;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.menu.Button;
import me.vifez.core.util.menu.Menu;
import me.vifez.core.util.CC;
import me.vifez.core.util.ItemBuilder;
import me.vifez.core.util.WoolColor;
import me.vifez.core.rank.Rank;
import me.vifez.core.rank.packets.RankReloadPacket;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class RankColorMenu extends Menu {

    private Rank rank;

    private AtomicReference<String> color = new AtomicReference<>("");
    private AtomicBoolean bold = new AtomicBoolean(false);
    private AtomicBoolean italics = new AtomicBoolean(false);

    public RankColorMenu(Rank rank) {
        this.rank = rank;

        setSize(27);

        setUpdate(true);
    }

    @Override
    public String getTitle(Player player) {
        return rank.getColoredName() + " &7- &fColor";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Map.Entry<String, String> entry : CC.getColorMap().entrySet()) {
            buttons.put(buttons.size(), new Button() {

                private String name = CC.translate(entry.getValue() + entry.getKey());

                @Override
                public ItemStack getButtonItem(Player player) {
                    return new ItemBuilder(new ItemStack(Material.WOOL, 1, WoolColor.fromChatColor(entry.getValue())))
                            .setName(name)
                            .setLore(
                                    "&6Left-click &eto set the color to &f" + name + "&e."
                            )
                            .build();
                }

                @Override
                public void clicked(Player player, ClickType clickType) {
                    color.set(entry.getValue());
                }

            });
        }

        buttons.put(18, new Button() {

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.PAPER)
                        .setName((bold.get() ? "&a" : "&c") + "&lBold")
                        .setLore(
                                "&6Left-click &eto toggle bold."
                        )
                        .build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                bold.set(!bold.get());
            }

        });

        buttons.put(19, new Button() {

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.PAPER)
                        .setName((italics.get() ? "&a" : "&c") + "&oItalics")
                        .setLore(
                                "&6Left-click &eto toggle italics."
                        )
                        .build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                italics.set(!italics.get());
            }

        });

        buttons.put(26, new Button() {

            {

                if (bold.get()) {
                    color.set(color.get() + "&l");
                }

                if (italics.get()) {
                    color.set(color.get() + "&o");
                }

            }

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(new ItemStack(Material.WOOL, 1, (byte) 5))
                        .setName("&aConfirm color")
                        .setLore(
                                "&6Left-click &eto set the color.",
                                kCoreConstant.INDENT + "&eCurrent color&7: &f" + CC.translate(color.get()) + "Example"
                        )
                        .build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                player.sendMessage(CC.YELLOW + "Set color of rank " + CC.WHITE + rank.getColoredName() + CC.YELLOW + " to " + CC.WHITE + CC.translate(color.get()) + rank.getName() + CC.YELLOW + '.');
                player.closeInventory();
                rank.setColor(color.get());
                rank.save();
                new RankReloadPacket(rank).send();
            }

        });


        return buttons;
    }

}