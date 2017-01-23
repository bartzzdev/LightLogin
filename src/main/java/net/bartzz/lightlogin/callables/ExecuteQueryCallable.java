package net.bartzz.lightlogin.callables;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;

public class ExecuteQueryCallable implements Callable<ResultSet>
{
    private PreparedStatement preparedStatement;

    public ExecuteQueryCallable(PreparedStatement preparedStatement)
    {
        this.preparedStatement = preparedStatement;
    }

    @Override
    public ResultSet call() throws Exception
    {
        return this.preparedStatement.executeQuery();
    }

    public void close()
    {
        try
        {
            this.preparedStatement.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
