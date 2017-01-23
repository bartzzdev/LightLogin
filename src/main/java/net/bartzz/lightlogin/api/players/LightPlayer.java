package net.bartzz.lightlogin.api.players;

import net.bartzz.lightlogin.api.players.messaging.Messaging;

import java.util.UUID;

public interface LightPlayer extends Messaging
{
    UUID getPlayerId();

    String getPlayerName();

    Account getAccountType();

    void setAccountType(Account accountType);
}
