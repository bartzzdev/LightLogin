package net.bartzzdev.lightlogin.api;

import net.bartzzdev.lightlogin.api.players.LightPlayerManager;
import net.bartzzdev.lightlogin.api.storage.database.Database;
import org.bukkit.command.CommandSender;

import java.util.concurrent.Future;

public interface LightLoginAPI {

    void buildCache();

    Future<String> getResult();

    Database getLoginDatabase();

    void sendPrefixedMessage(CommandSender commandSender, String message);

    LightPlayerManager getPlayerManager();

}