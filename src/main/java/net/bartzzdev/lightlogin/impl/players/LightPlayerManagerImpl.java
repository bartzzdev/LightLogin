package net.bartzzdev.lightlogin.impl.players;

import net.bartzzdev.lightlogin.api.data.ObjectInitializer;
import net.bartzzdev.lightlogin.api.players.LightPlayer;
import net.bartzzdev.lightlogin.api.players.LightPlayerManager;
import net.bartzzdev.lightlogin.utils.IOUtils;

import java.util.*;

public class LightPlayerManagerImpl implements LightPlayerManager, ObjectInitializer<LightPlayerManager> {

    private Map<UUID, LightPlayer> players;
    private Map<UUID, String> premiumNames;


    @Override
    public Map<UUID, LightPlayer> getPlayers() {
        return this.players;
    }

    @Override
    public Map<UUID, String> getPremiumNames() {
        return this.premiumNames;
    }

    @Override
    public LightPlayer create(UUID uuid, String playerName) {
        LightPlayer lPlayer = new LightPlayerImpl(uuid, playerName);
        this.players.put(uuid, lPlayer);
        if (!IOUtils.getContent("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().split("-")).isEmpty()) {
            this.premiumNames.put(uuid, playerName);
        }
        return lPlayer;
    }

    @Override
    public LightPlayerManager register() {
        this.players = new HashMap<>();
        this.premiumNames = new HashMap<>();
        return this;
    }
}
