package net.bartzz.lightlogin.impl.players.cracked;

import net.bartzz.lightlogin.api.players.Account;
import net.bartzz.lightlogin.impl.players.LightPlayerImpl;

import java.util.UUID;

public class CrackedPlayer extends LightPlayerImpl
{
    private String password;

    public CrackedPlayer(UUID playerId, String playerName)
    {
        super(playerId, playerName);
    }

    @Override
    public Account getAccountType()
    {
        return Account.CRACKED;
    }

    public String getPassword()
    {
        return this.password;
    }

    public void changePassword(String newPass)
    {
        this.password = newPass;
    }
}