package net.bartzz.lightlogin.impl.storage.database;

import net.bartzz.lightlogin.api.storage.database.Database;
import net.bartzz.lightlogin.api.threads.Executor;
import net.bartzz.lightlogin.api.threads.ExecutorInitializer;
import net.bartzz.lightlogin.callables.ConnectionCallable;

import java.sql.Connection;

public class DatabaseImpl implements Database
{
    private Connection connection;
    private String host;
    private String user;
    private String password;
    private String database;
    private Integer port;

    public DatabaseImpl(String host, String user, String password, String database, Integer port)
    {
        this.host = host;
        this.user = user;
        this.password = password;
        this.database = database;
        this.port = port;
    }

    @Override
    public void connect()
    {
        ConnectionCallable callable = new ConnectionCallable(this.port, this.host, this.user, this.password, this.database);
        Executor<Boolean> executor = new ExecutorInitializer<Boolean>().newExecutor(callable);
        executor.execute();
    }

    @Override
    public void disconnect()
    {
        ConnectionCallable callable = new ConnectionCallable(this.connection, true);
        Executor<Boolean> executor = new ExecutorInitializer<Boolean>().newExecutor(callable);
        executor.execute();
    }

    @Override
    public void reconnect()
    {
        this.connect();
    }

    @Override
    public Connection getConnection()
    {
        return this.connection;
    }
}
