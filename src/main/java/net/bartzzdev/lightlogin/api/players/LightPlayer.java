package net.bartzzdev.lightlogin.api.players;

import net.bartzzdev.lightlogin.api.data.ObjectHolder;
import net.bartzzdev.lightlogin.api.data.Changeable;
import net.bartzzdev.lightlogin.enums.Account;

public interface LightPlayer extends ObjectHolder<LightPlayer>, Changeable {

    String getPlayerName();

    Account getAccountState();

    void setAccount(Account account);

    long lastLogged();

    void setLastLogged(long lastLogged);
}
