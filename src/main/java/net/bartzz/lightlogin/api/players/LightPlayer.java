package net.bartzz.lightlogin.api.players;

import net.bartzz.lightlogin.api.files.enums.Messages;

import java.util.UUID;

public interface LightPlayer
{
    UUID getPlayerId();

    String getPlayerName();

    Account getAccountType();

    void setAccountType(Account accountType);

    void sendPrefixedMessage(Messages message);
}
