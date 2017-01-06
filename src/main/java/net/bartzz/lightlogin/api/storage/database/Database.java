package net.bartzz.lightlogin.api.storage.database;

import java.sql.Connection;

public interface Database
{
    void connect();

    void disconnect();

    void reconnect();

    Connection getConnection();
}
