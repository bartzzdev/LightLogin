package net.bartzz.lightlogin.impl.players;

import net.bartzz.lightlogin.api.players.LightPlayer;
import net.bartzz.lightlogin.api.players.LightPlayerManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LightPlayerManagerImpl implements LightPlayerManager
{
    private final Map<UUID, LightPlayer> playersMap = new HashMap<>();

    @Override
    public Map<UUID, LightPlayer> getPlayersMap()
    {
        return this.playersMap;
    }

    @Override
    public LightPlayer get(UUID playerId)
    {
        return this.playersMap.get(playerId);
    }

    @Override
    public LightPlayer get(String playerName)
    {
        return this.playersMap.values()
                       .stream()
                       .filter(lPlayer -> lPlayer.getPlayerName().equalsIgnoreCase(playerName))
                       .findFirst()
                       .get();
    }

    @Override
    public LightPlayer create(UUID playerId, String playerName)
    {
        if (!this.playersMap.containsKey(playerId))
        {
            this.playersMap.put(playerId, new LightPlayerImpl(playerId, playerName));
        }

        return this.get(playerId);
    }
}
