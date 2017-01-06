package net.bartzz.lightlogin.api.players;

import java.util.Map;
import java.util.UUID;

public interface LightPlayerManager
{
    Map<UUID, LightPlayer> getPlayersMap();

    LightPlayer get(UUID playerId);

    LightPlayer get(String playerName);

    LightPlayer create(UUID playerId, String playerName);
}
