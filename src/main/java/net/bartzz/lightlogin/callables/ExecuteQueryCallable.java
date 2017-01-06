package net.bartzz.lightlogin.callables;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
}
