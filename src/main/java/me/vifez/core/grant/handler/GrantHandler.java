package me.vifez.core.grant.handler;

import me.vifez.core.grant.Grant;
import me.vifez.core.util.Pair;
import me.vifez.core.kCore;
import me.vifez.core.handler.Handler;
import me.vifez.core.profile.Profile;
import me.vifez.core.rank.Rank;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GrantHandler extends Handler {

    private Map<UUID, Profile> grantProfile = new HashMap<>();
    private Map<UUID, Rank> grantRank = new HashMap<>();
    private Map<UUID, Pair<Profile, Grant>> removing = new HashMap<>();

    public GrantHandler(kCore core) {
        super(core);
    }

    public Map<UUID, Rank> getGrantRank() {
        return grantRank;
    }

    public Map<UUID, Profile> getGrantProfile() {
        return grantProfile;
    }

    public Map<UUID, Pair<Profile, Grant>> getRemoving() {
        return removing;
    }

}