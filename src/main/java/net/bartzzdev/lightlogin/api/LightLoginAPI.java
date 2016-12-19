package net.bartzzdev.lightlogin.api;

import net.bartzzdev.lightlogin.api.players.LightPlayerManager;
import net.bartzzdev.lightlogin.api.storage.database.Database;
import net.bartzzdev.lightlogin.api.yaml.LightConfiguration;
import net.bartzzdev.lightlogin.api.yaml.LightMessages;
import net.bartzzdev.lightlogin.enums.Storage;
import org.bukkit.command.CommandSender;

import java.util.concurrent.Future;
import java.util.logging.Level;

public interface LightLoginAPI {

    void buildCache();

    Future<String> getResult();

    Database getLoginDatabase();

    void sendPrefixedMessage(CommandSender commandSender, String message);

    void sendPrefixedLog(Level level, String message);

    LightPlayerManager getPlayerManager();

    Storage getStorageType();

    LightConfiguration getConfiguration();

    LightMessages getMessages();
}
