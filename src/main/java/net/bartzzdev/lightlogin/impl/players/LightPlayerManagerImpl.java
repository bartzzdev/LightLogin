package net.bartzzdev.lightlogin.impl.players;

import net.bartzzdev.lightlogin.api.data.ObjectInitializer;
import net.bartzzdev.lightlogin.api.players.LightPlayer;
import net.bartzzdev.lightlogin.api.players.LightPlayerManager;
import net.bartzzdev.lightlogin.utils.IOUtils;

import java.util.*;

public class LightPlayerManagerImpl implements LightPlayerManager, ObjectInitializer<LightPlayerManager> {

    private Set<LightPlayer> players = new HashSet<>();
    private Map<UUID, String> premiumNames = new HashMap<>();

    @Override
    public Set<LightPlayer> getPlayers() {
        return this.players;
    }

    @Override
    public Map<UUID, String> getPremiumNames() {
        return this.premiumNames;
    }

    @Override
    public LightPlayer create(UUID uuid, String playerName) {
        LightPlayer lPlayer = new LightPlayerImpl(uuid, playerName);
        this.players.add(lPlayer);
        if (!IOUtils.getContent("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().split("-")).isEmpty()) {
            this.premiumNames.put(uuid, playerName);
        }
        return lPlayer;
    }

    @Override
    public LightPlayerManager register() {
        return this;
    }
}
