package me.vifez.core.punishment;

import me.vifez.core.punishment.serialization.PunishmentSerialization;
import me.vifez.core.util.CC;
import me.vifez.core.util.TimeUtil;
import me.vifez.core.kCore;
import org.apache.commons.lang.time.DurationFormatUtils;

import java.util.UUID;

public class Punishment {

    public static final PunishmentSerialization SERIALIZATION = new PunishmentSerialization();

    private PunishmentType type;
    private long duration;

    private long addedAt;
    private String addedReason;
    private UUID addedBy;

    private long removedAt;
    private String removedReason;
    private UUID removedBy;

    public Punishment(PunishmentType type, long duration, long addedAt, String addedReason, UUID addedBy) {
        this.type = type;
        this.duration = duration;
        this.addedAt = addedAt;
        this.addedReason = addedReason;
        this.addedBy = addedBy;
    }

    public String getDurationText() {
        if (isPermanent()) {
            return "Permanent";
        }

        return DurationFormatUtils.formatDurationWords(duration, true, true);
    }

    public String getExpiresIn() {
        if (isRemoved()) {
            return "Removed";
        }

        if (!isActive()) {
            return "Expired";
        }

        if (isPermanent()) {
            return "Never";
        }

        return DurationFormatUtils.formatDurationWords(getExpiry(), true, true);
    }

    public long getExpiry() {
        return (addedAt + duration) - System.currentTimeMillis();
    }

    public String getAddedByName() {
        if (addedBy != null) {
            return kCore.getInstance().getProfileHandler().getProfile(addedBy).getColoredName();
        }

        return CC.DARK_RED + "Console";
    }

    public String getRemovedByName() {
        if (removedBy != null) {
            return kCore.getInstance().getProfileHandler().getProfile(removedBy).getColoredName();
        }

        return CC.DARK_RED + "Console";
    }

    public String getFormattedAddedAt() {
        return TimeUtil.formatDate(addedAt);
    }

    public String getFormattedRemovedAt() {
        return TimeUtil.formatDate(removedAt);
    }

    public boolean isActive() {
        return (System.currentTimeMillis() <= addedAt + duration || isPermanent()) && !isRemoved();
    }

    public boolean isRemoved() {
        return removedReason != null;
    }

    public boolean isPermanent() {
        return duration == Long.MAX_VALUE;
    }

    public void setRemoved(long removedAt, String removedReason) {
        setRemoved(removedAt, removedReason, null);
    }

    public void setRemoved(long removedAt, String removedReason, UUID removedBy) {
        this.removedAt = removedAt;
        this.removedReason = removedReason;
        this.removedBy = removedBy;
    }

    public PunishmentType getType() {
        return type;
    }

    public long getDuration() {
        return duration;
    }

    public long getAddedAt() {
        return addedAt;
    }

    public String getAddedReason() {
        return addedReason;
    }

    public UUID getAddedBy() {
        return addedBy;
    }

    public long getRemovedAt() {
        return removedAt;
    }

    public String getRemovedReason() {
        return removedReason;
    }

    public UUID getRemovedBy() {
        return removedBy;
    }

}