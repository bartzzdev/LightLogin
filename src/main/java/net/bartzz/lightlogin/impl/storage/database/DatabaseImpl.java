package net.bartzz.lightlogin.impl.storage.database;

import net.bartzz.lightlogin.api.data.DataInitializer;
import net.bartzz.lightlogin.api.storage.database.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseImpl implements Database, DataInitializer<Connection>
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
    public Connection register()
    {
        try
        {
            return this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.user, this.password);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void connect()
    {
        try
        {
            if (this.connection != null && this.connection.isClosed())
            {
                this.register();
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect()
    {
        try
        {
            if (this.connection != null && !this.connection.isClosed())
            {
                this.connection.close();
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
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
