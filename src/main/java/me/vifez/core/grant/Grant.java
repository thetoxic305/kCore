package me.vifez.core.grant;

import me.vifez.core.grant.serialization.GrantSerialization;
import me.vifez.core.util.CC;
import me.vifez.core.util.TimeUtil;
import me.vifez.core.kCore;
import me.vifez.core.rank.Rank;
import org.apache.commons.lang.time.DurationFormatUtils;

import java.util.UUID;

public class Grant {

    public static final GrantSerialization SERIALIZATION = new GrantSerialization();

    private Rank rank;
    private long duration;

    private long addedAt;
    private String addedReason;
    private UUID addedBy;

    private long removedAt;
    private String removedReason;
    private UUID removedBy;

    public Grant(Rank rank, long duration, long addedAt, String addedReason, UUID addedBy) {
        this.rank = rank;
        this.duration = duration;
        this.addedAt = addedAt;
        this.addedReason = addedReason;
        this.addedBy = addedBy;
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

        return DurationFormatUtils.formatDurationWords((addedAt + duration) - System.currentTimeMillis(), true, true);
    }

    public String getDurationText() {
        if (isPermanent()) {
            return "Permanent";
        }

        return DurationFormatUtils.formatDurationWords(duration, true, true);
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

    public Rank getRank() {
        return rank;
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