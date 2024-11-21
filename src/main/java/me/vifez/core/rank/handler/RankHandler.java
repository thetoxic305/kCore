package me.vifez.core.rank.handler;

import me.vifez.core.kCoreConstant;
import me.vifez.core.rank.Rank;
import me.vifez.core.util.mongo.subscribers.RequestSubscriber;
import me.vifez.core.util.Pair;
import me.vifez.core.kCore;
import me.vifez.core.handler.Handler;
import org.bson.Document;

import java.util.*;

public class RankHandler extends Handler {

    private final Set<Rank> ranks = new HashSet<>();

    private final Map<UUID, Pair<UUID, String>> editingProperties = new HashMap<>();

    public RankHandler(kCore core) {
        super(core);

        loadRanks();
    }

    private void loadRanks() {
        Rank.COLLECTION.find().subscribe(new RequestSubscriber(Long.MAX_VALUE) {

            @Override
            public void onNext(Document document) {
                Set<String> inheritString = kCoreConstant.GSON.fromJson(document.getString("inherits"), kCoreConstant.SET_STRING_TYPE);
                Set<UUID> inherits = new HashSet<>();
                for (String string : inheritString)
                    inherits.add(UUID.fromString(string));

                Rank rank = new Rank(
                        document.getString("name"),
                        UUID.fromString(document.getString("uuid")),
                        document.getBoolean("default"),
                        document.getInteger("priority"),
                        document.getString("color"),
                        document.getString("prefix"),
                        document.getString("suffix"),
                        kCoreConstant.GSON.fromJson(document.getString("permissions"), kCoreConstant.SET_STRING_TYPE),
                        inherits
                );

                ranks.add(rank);
            }

            @Override
            public void onComplete() {
                for (Rank rank : ranks) {
                    rank.loadInherits();
                }

                if (getDefaultRank() == null) {
                    Rank rank = new Rank("Default", UUID.randomUUID());
                    rank.setDefault(true);
                    rank.save();
                    ranks.add(rank);
                }

                core.getProfileHandler().loadProfiles();
            }

        });
    }

    public Rank getDefaultRank() {
        for (Rank rank : ranks) {
            if (rank.isDefault()) {
                return rank;
            }
        }
        return null;
    }

    public Rank getRank(String name) {
        for (Rank rank : ranks) {
            if (rank.getName().equalsIgnoreCase(name)) {
                return rank;
            }
        }
        return null;
    }

    public Rank getRank(UUID uuid) {
        for (Rank rank : ranks) {
            if (rank.getUUID().equals(uuid)) {
                return rank;
            }
        }
        return null;
    }

    public List<Rank> getSortedRanks() {
        List<Rank> toReturn = new ArrayList<>(ranks);
        toReturn.sort(Comparator.comparingInt(Rank::getPriority));
        Collections.reverse(toReturn);
        return toReturn;
    }

    public Set<Rank> getRanks() {
        return ranks;
    }

    public Map<UUID, Pair<UUID, String>> getEditingProperties() {
        return editingProperties;
    }

}