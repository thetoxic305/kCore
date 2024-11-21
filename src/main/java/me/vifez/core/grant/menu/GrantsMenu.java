package me.vifez.core.grant.menu;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.menu.Button;
import me.vifez.core.util.menu.paged.filterable.FilterablePagedMenu;
import me.vifez.core.util.menu.paged.filterable.PageFilter;
import me.vifez.core.util.CC;
import me.vifez.core.util.ItemBuilder;
import me.vifez.core.util.Pair;
import me.vifez.core.kCore;
import me.vifez.core.grant.Grant;
import me.vifez.core.profile.Profile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class GrantsMenu extends FilterablePagedMenu<Grant> {

    private final kCore core;
    private Profile profile;

    public GrantsMenu(kCore core, Profile profile) {
        this.core = core;
        this.profile = profile;

        setEntriesPerPage(9);
    }

    @Override
    public String getTitle(Player player) {
        return profile.getColoredName() + CC.GRAY + " - " + CC.WHITE + "Grants &b(" + profile.getGrants().size() + ")";
    }

    @Override
    public Map<Integer, Button> getAllButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        AtomicInteger at = new AtomicInteger(0);

        Profile executor = core.getProfileHandler().getProfile(player.getUniqueId());

        buttonLoop:
        for (Grant grant : profile.getSortedGrants()) {
            for (PageFilter<Grant> filter : getFilters()) {
                if (!filter.test(grant)) {
                    continue buttonLoop;
                }
            }

            buttons.put(buttons.size(), new Button() {

                @Override
                public ItemStack getButtonItem(Player player) {
                    at.getAndIncrement();

                    List<String> lore = new ArrayList<>();

                    lore.add(kCoreConstant.ITEM_LINE);
                    lore.add(kCoreConstant.INDENT + "&bRank&7: &f" + grant.getRank().getColoredName());
                    lore.add(kCoreConstant.INDENT + "&bDuration&7: &f" + grant.getDurationText());
                    lore.add(kCoreConstant.INDENT + "&bExpires&7: &f" + grant.getExpiresIn());
                    lore.add(kCoreConstant.ITEM_LINE);
                    lore.add(kCoreConstant.INDENT + "&bAdded at&7: &f" + grant.getFormattedAddedAt());
                    lore.add(kCoreConstant.INDENT + "&bAdded reason&7: &f" + grant.getAddedReason());
                    lore.add(kCoreConstant.INDENT + "&bAdded by&7: &f" + grant.getAddedByName());

                    if (grant.isRemoved()) {
                        lore.add(kCoreConstant.ITEM_LINE);
                        lore.add(kCoreConstant.INDENT + "&bRemoved at&7: &f" + grant.getFormattedRemovedAt());
                        lore.add(kCoreConstant.INDENT + "&bRemoved reason&7: &f" + grant.getRemovedReason());
                        lore.add(kCoreConstant.INDENT + "&bRemoved by&7: &f" + grant.getRemovedByName());
                    }

                    lore.add(kCoreConstant.ITEM_LINE);

                    if (grant.getRank().getPriority() < executor.getRank().getPriority() && !grant.getAddedReason().equals("Default Grant") && grant.isActive()) {
                        lore.add("&bLeft-click &fto remove this grant.");
                        lore.add(kCoreConstant.ITEM_LINE);
                    }


                    return new ItemBuilder(Material.PAPER)
                            .setName("&f#" + at.get())
                            .setLore(lore)
                            .build();
                }

                @Override
                public void clicked(Player player, ClickType clickType) {
                    if (!clickType.isLeftClick() || !grant.isActive() || (grant.getRank().getPriority() >= executor.getRank().getPriority()) || grant.getAddedReason().equals("Default Grant")) {
                        return;
                    }

                    player.closeInventory();
                    core.getGrantHandler().getRemoving().put(player.getUniqueId(), new Pair<>(profile, grant));
                    player.sendMessage(CC.AQUA + "Please enter a reason for removing this grant.");
                }

            });

        }

        return buttons;
    }

    @Override
    public List<PageFilter<Grant>> generateFilters() {
        List<PageFilter<Grant>> filters = new ArrayList<>();
        filters.add(new PageFilter<>("All", grant -> true));
        filters.add(new PageFilter<>("Active", Grant::isActive));
        return filters;
    }

}