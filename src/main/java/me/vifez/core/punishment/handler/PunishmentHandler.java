package me.vifez.core.punishment.handler;

import me.vifez.core.kCoreConstant;
import me.vifez.core.punishment.Punishment;
import me.vifez.core.punishment.PunishmentType;
import me.vifez.core.util.CC;
import me.vifez.core.util.Pair;
import me.vifez.core.util.StringUtil;
import me.vifez.core.kCore;
import me.vifez.core.handler.Handler;
import me.vifez.core.profile.Profile;

import java.util.*;

public class PunishmentHandler extends Handler {

    private Map<UUID, Pair<Profile, Punishment>> removing = new HashMap<>();

    public PunishmentHandler(kCore core) {
        super(core);
    }

    public Pair<String, String> getRemovalBroadcastMessage(Profile profile, Punishment punishment, String executor, boolean silent) {
        if (executor == null) {
            executor = CC.DARK_RED + "Console";
        }

        String name = CC.GREEN + (punishment.getType() == PunishmentType.BAN ? "unbanned" : "unmuted");

        String message = CC.translate((silent ? "&6[Silent] " : "") + profile.getColoredName() + " &ehas been " + name + " &eby " + executor + "&e.");
        String hover = StringUtil.listToString(CC.translate(Arrays.asList(
                kCoreConstant.CHAT_LINE,
                kCoreConstant.INDENT + "&eReason&7: &f" + punishment.getRemovedReason(),
                kCoreConstant.CHAT_LINE
        )));

        return new Pair<>(message, hover);
    }

    public Pair<String, String> getBroadcastMessage(Profile profile, Punishment punishment, String executor, boolean silent) {
        if (executor == null) {
            executor = CC.DARK_RED + "Console";
        }

        String message = CC.translate((silent ? "&6[Silent] " : "") + profile.getColoredName() + " &ehas been " + getName(punishment.getType(), true) + " &eby " + executor + "&e.");
        String hover;

        if(punishment.getType() != PunishmentType.KICK && punishment.getType() != PunishmentType.WARN) {
            hover = StringUtil.listToString(CC.translate(Arrays.asList(
                    kCoreConstant.CHAT_LINE,
                    kCoreConstant.INDENT + "&eReason&7: &f" + punishment.getAddedReason(),
                    kCoreConstant.INDENT + "&eDuration&7: &f" + punishment.getDurationText(),
                    kCoreConstant.CHAT_LINE
            )));
        } else {
            hover = StringUtil.listToString(CC.translate(Arrays.asList(
                    kCoreConstant.CHAT_LINE,
                    kCoreConstant.INDENT + "&eReason&7: &f" + punishment.getAddedReason(),
                    kCoreConstant.CHAT_LINE
            )));
        }

        return new Pair<>(message, hover);
    }

    public String getMessage(Punishment punishment) {
        return getMessage(punishment, null);
    }

    public String getMessage(Punishment punishment, Profile alt) {
        PunishmentType type = punishment.getType();
        List<String> message = new ArrayList<>();

        if (type == PunishmentType.BLACKLIST || type == PunishmentType.BAN) {
            message.add("&cYour account has been " + getName(punishment.getType(), true) + " &cfrom Test.");

            if (alt != null) {
                message.add("");
                message.add("&cThis punishment is in relation to " + alt.getColoredName() + '.');
            }

            message.add("");
            message.add("&cExecutor&7: &f" + punishment.getAddedByName());
            message.add("&cReason&7: &f" + punishment.getAddedReason());
            if (type == PunishmentType.BAN) {
                message.add("&cExpires&7: &f" + punishment.getExpiresIn());
            }
            message.add("");
            message.add(type == PunishmentType.BLACKLIST ? "&cThis punishment cannot be appealed." : "&cThis punishment can be appealed at &fdiscord.test.com&c.");
        }

        if (type == PunishmentType.MUTE) {
            message.add("&cYou are currently " + getName(punishment.getType(), true) + " &cfor &f" + punishment.getAddedReason() + " &cby " + punishment.getAddedByName() + " &cand may not talk.");
            message.add("&cThis punishment will expire in &f" + punishment.getExpiresIn().toLowerCase() + "&c.");
        }

        if (type == PunishmentType.WARN) {
            message.add("&cYou have been " + getName(punishment.getType(), true) + " &cfor &f" + punishment.getAddedReason() + " &cby " + punishment.getAddedByName() + "&c.");
        }

        if (type == PunishmentType.KICK) {
            message.add("&cYou have been " + getName(punishment.getType(), true) + "&c.");
            message.add("");
            message.add("&cExecutor&7: &f" + punishment.getAddedByName());
            message.add("&cReason&7: &f" + punishment.getAddedReason());
        }

        return StringUtil.listToString(CC.translate(message));
    }

    public String getName(PunishmentType type, boolean ed) {
        String message = "";
        switch (type) {
            case BLACKLIST: {
                message = CC.DARK_RED + "blacklist";
                break;
            }
            case BAN: {
                message = CC.RED + "ban";
                break;
            }
            case MUTE: {
                message = CC.PURPLE + "mute";
                break;
            }
            case WARN: {
                message = CC.GOLD + "warn";
                break;
            }
            case KICK: {
                message = CC.AQUA + "kick";
                break;
            }
        }
        return (ed ? message.replace("e", "") : message) + (ed ? (type == PunishmentType.BAN ? "n" : "") + "ed" : "");
    }

    public Map<UUID, Pair<Profile, Punishment>> getRemoving() {
        return removing;
    }

}