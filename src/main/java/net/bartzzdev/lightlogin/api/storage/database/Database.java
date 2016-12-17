package net.bartzzdev.lightlogin.api.storage.database;

import java.sql.Connection;

public interface Database {

    Connection getConnection();

    boolean connect();

    void disconnect();

    void reconnect();

    boolean isConnected();

    void setConnection(Connection connection);
}
