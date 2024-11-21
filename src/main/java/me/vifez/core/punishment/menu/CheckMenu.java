package me.vifez.core.punishment.menu;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.menu.Button;
import me.vifez.core.util.menu.paged.filterable.FilterablePagedMenu;
import me.vifez.core.util.menu.paged.filterable.PageFilter;
import me.vifez.core.util.CC;
import me.vifez.core.util.ItemBuilder;
import me.vifez.core.util.Pair;
import me.vifez.core.kCore;
import me.vifez.core.profile.Profile;
import me.vifez.core.punishment.Punishment;
import me.vifez.core.punishment.PunishmentType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CheckMenu extends FilterablePagedMenu<Punishment> {

    private final kCore core;
    private Profile profile;

    public CheckMenu(kCore core, Profile profile) {
        this.core = core;
        this.profile = profile;

        setEntriesPerPage(9);
    }

    @Override
    public String getTitle(Player player) {
        return profile.getColoredName() + " &7- &fPunishments &e(" + profile.getPunishments().size() + ")";
    }

    @Override
    public Map<Integer, Button> getAllButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        AtomicInteger at = new AtomicInteger(0);

        buttonLoop:
        for (Punishment punishment : profile.getSortedPunishments()) {
            for (PageFilter<Punishment> filter : getFilters()) {
                if (!filter.test(punishment)) {
                    continue buttonLoop;
                }
            }

            buttons.put(buttons.size(), new Button() {

                @Override
                public ItemStack getButtonItem(Player player) {
                    at.getAndIncrement();
                    List<String> lore = new ArrayList<>();

                    lore.add(kCoreConstant.ITEM_LINE);
                    lore.add(kCoreConstant.INDENT + "&eType&7: &f" + core.getPunishmentHandler().getName(punishment.getType(), false));
                    lore.add(kCoreConstant.INDENT + "&eDuration&7: &f" + punishment.getDurationText());
                    lore.add(kCoreConstant.INDENT + "&eExpires&7: &f" + punishment.getExpiresIn());
                    lore.add(kCoreConstant.ITEM_LINE);
                    lore.add(kCoreConstant.INDENT + "&eAdded at&7: &f" + punishment.getFormattedAddedAt());
                    lore.add(kCoreConstant.INDENT + "&eAdded reason&7: &f" + punishment.getAddedReason());
                    lore.add(kCoreConstant.INDENT + "&eAdded by&7: &f" + punishment.getAddedByName());

                    if (punishment.isRemoved()) {
                        lore.add(kCoreConstant.ITEM_LINE);
                        lore.add(kCoreConstant.INDENT + "&eRemoved at&7: &f" + punishment.getFormattedRemovedAt());
                        lore.add(kCoreConstant.INDENT + "&eRemoved reason&7: &f" + punishment.getRemovedReason());
                        lore.add(kCoreConstant.INDENT + "&eRemoved by&7: &f" + punishment.getRemovedByName());
                    }
                    lore.add(kCoreConstant.ITEM_LINE);

                    if (player.hasPermission("core.admin") && punishment.isActive() && punishment.getType() != PunishmentType.BLACKLIST && punishment.getType() != PunishmentType.WARN && punishment.getType() != PunishmentType.KICK) {
                        lore.add("&6Left-click &eto remove this punishment");
                        lore.add(kCoreConstant.ITEM_LINE);
                    }

                    return new ItemBuilder(Material.PAPER)
                            .setName("&f#" + at.get())
                            .setLore(lore)
                            .build();
                }

                @Override
                public void clicked(Player player, ClickType clickType) {
                    if (!clickType.isLeftClick() || !player.hasPermission("core.admin") || !punishment.isActive() || punishment.getType() == PunishmentType.BLACKLIST || punishment.getType() == PunishmentType.WARN || punishment.getType() == PunishmentType.KICK) {
                        return;
                    }

                    player.closeInventory();
                    core.getPunishmentHandler().getRemoving().put(player.getUniqueId(), new Pair<>(profile, punishment));
                    player.sendMessage(CC.YELLOW + "Please enter a reason for removing this punishment.");
                }

            });

        }

        return buttons;
    }

    @Override
    public List<PageFilter<Punishment>> generateFilters() {
        List<PageFilter<Punishment>> filters = new ArrayList<>();

        filters.add(new PageFilter<>("All", punishment -> true));
        filters.add(new PageFilter<>("Active", Punishment::isActive));
        filters.add(new PageFilter<>("Blacklists", punishment -> punishment.getType() == PunishmentType.BLACKLIST));
        filters.add(new PageFilter<>("Bans", punishment -> punishment.getType() == PunishmentType.BAN));
        filters.add(new PageFilter<>("Mutes", punishment -> punishment.getType() == PunishmentType.MUTE));
        filters.add(new PageFilter<>("Warns", punishment -> punishment.getType() == PunishmentType.WARN));
        filters.add(new PageFilter<>("Kicks", punishment -> punishment.getType() == PunishmentType.KICK));

        return filters;
    }

}