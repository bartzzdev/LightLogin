package net.bartzzdev.lightlogin.impl.players;

import net.bartzzdev.lightlogin.api.players.LightPlayer;
import net.bartzzdev.lightlogin.enums.Account;

import java.util.UUID;

public class LightPlayerImpl implements LightPlayer {

    private final UUID uuid;
    private String playerName;
    private Account account;
    private long lastLogged;
    private boolean changed;

    public LightPlayerImpl(UUID uuid, String playerName) {
        this.uuid = uuid;
        this.playerName = playerName;
        this.account = Account.NOT_AUTHORIZED;
    }

    @Override
    public String getPlayerName() {
        return this.playerName;
    }

    @Override
    public Account getAccountState() {
        return this.account;
    }

    @Override
    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public long lastLogged() {
        return this.lastLogged;
    }

    @Override
    public void setLastLogged(long lastLogged) {
        this.lastLogged = lastLogged;
    }

    @Override
    public boolean changed() {
        return this.changed;
    }

    @Override
    public void change() {
        this.changed = true;
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public LightPlayer save() {
        //TODO saving player object.
        return null;
    }

    @Override
    public LightPlayer load() {
        //TODO loading player object.
        return null;
    }
}
