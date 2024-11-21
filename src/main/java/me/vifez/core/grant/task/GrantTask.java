package me.vifez.core.grant.task;

import me.vifez.core.kCore;
import me.vifez.core.profile.Profile;
import org.bukkit.scheduler.BukkitRunnable;

public class GrantTask extends BukkitRunnable {

    private final kCore core;

    public GrantTask(kCore core) {
        this.core = core;
    }

    @Override
    public void run() {
        for (Profile profile : core.getProfileHandler().getProfiles()) {
            profile.checkGrants();
            profile.setupPlayer();
        }
    }

}