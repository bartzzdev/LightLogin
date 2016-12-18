package net.bartzzdev.lightlogin.impl.storage.database;

import net.bartzzdev.lightlogin.api.storage.database.Database;
import net.bartzzdev.lightlogin.api.storage.database.query.Query;
import net.bartzzdev.lightlogin.api.thread.Executor;
import net.bartzzdev.lightlogin.api.thread.ExecutorInitializer;
import net.bartzzdev.lightlogin.enums.Configuration;
import net.bartzzdev.lightlogin.impl.storage.database.query.QueryImpl;
import net.bartzzdev.lightlogin.tasks.ConnectionCallable;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseImpl implements Database {

    private String host, user, password, database;
    private Integer port;
    private Connection connection;

    public DatabaseImpl(String host, String user, String password, String database, Integer port) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.database = database;
        this.port = port;
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public boolean connect() {
        ConnectionCallable connection = new ConnectionCallable("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.user, this.password);
        Executor<Boolean> connectionExecutor = new ExecutorInitializer(connection);
        return connectionExecutor.call();
    }

    @Override
    public void disconnect() {
        Executor<Boolean> connectionExecutor = new ExecutorInitializer(new ConnectionCallable(true));
        connectionExecutor.call();
    }

    @Override
    public void reconnect() {
        this.connect();
    }

    @Override
    public boolean isConnected() {
        try {
            return !this.connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
