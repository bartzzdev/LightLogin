package net.bartzz.lightlogin.api;

import net.bartzz.lightlogin.api.files.LightConfiguration;
import net.bartzz.lightlogin.api.files.LightMessages;
import net.bartzz.lightlogin.api.players.LightPlayerManager;
import net.bartzz.lightlogin.api.storage.database.Database;

public interface LightLoginAPI
{
    void buildCache();

    LightPlayerManager getPlayerManager();

    LightMessages getMessages();

    LightConfiguration getConfiguration();

    Database getLightDatabase();
}
