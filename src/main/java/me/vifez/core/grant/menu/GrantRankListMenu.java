package me.vifez.core.grant.menu;

import me.vifez.core.util.menu.Button;
import me.vifez.core.util.menu.Menu;
import me.vifez.core.util.CC;
import me.vifez.core.util.ItemBuilder;
import me.vifez.core.util.WoolColor;
import me.vifez.core.kCore;
import me.vifez.core.profile.Profile;
import me.vifez.core.rank.Rank;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class GrantRankListMenu extends Menu {

    private Profile profile;
    private final kCore core;

    public GrantRankListMenu(Profile profile, kCore core) {
        this.profile = profile;
        this.core = core;
    }

    @Override
    public String getTitle(Player player) {
        return "&6Select a rank";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Rank rank : core.getRankHandler().getSortedRanks()) {
            Profile executor = core.getProfileHandler().getProfile(player.getUniqueId());


            if (rank.getPriority() >= executor.getRank().getPriority()) {
                continue;
            }

            buttons.put(buttons.size(), new Button() {

                @Override
                public ItemStack getButtonItem(Player player) {
                    return new ItemBuilder(new ItemStack(Material.WOOL, 1, WoolColor.fromChatColor(rank.getRawColor())))
                            .setName(rank.getColoredName())
                            .setLore(
                                    "&6Left-click &eto grant " + profile.getColoredName() + " &erank " + rank.getColoredName() + "&e."
                            )
                            .build();
                }

                @Override
                public void clicked(Player player, ClickType clickType) {
                    if (!clickType.isLeftClick()) {
                        return;
                    }

                    player.closeInventory();
                    core.getGrantHandler().getGrantRank().put(player.getUniqueId(), rank);
                    core.getGrantHandler().getGrantProfile().put(player.getUniqueId(), profile);

                    player.sendMessage(CC.YELLOW + "Please enter a reason for this grant.");
                }

            });


        }

        return buttons;
    }

}