package net.bartzz.lightlogin.callables;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Callable;

public class ConnectionCallable implements Callable<Boolean>
{
    private boolean connected = false;
    private Connection connection;
    private String[] data = new String[]{};
    private Integer port;

    public ConnectionCallable(Integer port, String... data)
    {
        this.port = port;
        for (int i  = 0; i < 4; i++)
        {
            this.data[i] = data[i];
        }

        this.connected = connected;
    }

    public ConnectionCallable(Connection connection, boolean connected)
    {
        this.connection = connection;
        this.connected = connected;
    }


    @Override
    public Boolean call() throws Exception
    {
        if (this.connected)
        {
            if (this.connection == null || this.connection.isClosed())
            {
                return false;
            }

            this.connection.close();
            return true;
        } else
        {
            try
            {
                Runnable keepAlive = () -> {
                    while (true) try
                    {
                        wait(1000);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                };

                new Thread(keepAlive).start();

                this.connection = DriverManager.getConnection("jdbc:mysql://" + this.data[0] + ":" + this.port + "/" + this.data[3], this.data[1], this.data[2]);
                return true;
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
            return false;
        }
    }
}
