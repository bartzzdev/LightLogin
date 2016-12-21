package net.bartzzdev.lightlogin.api.players;

import java.util.Map;
import java.util.UUID;

public interface LightPlayerManager {

    Map<UUID, LightPlayer> getPlayers();

    Map<UUID, String> getPremiumNames();

    LightPlayer create(UUID uuid, String playerName);
}
