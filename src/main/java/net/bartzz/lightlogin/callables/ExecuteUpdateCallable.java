package net.bartzz.lightlogin.callables;

import java.sql.PreparedStatement;
import java.util.concurrent.Callable;

public class ExecuteUpdateCallable implements Callable<Integer>
{
    private PreparedStatement preparedStatement;

    public ExecuteUpdateCallable(PreparedStatement preparedStatement)
    {
        this.preparedStatement = preparedStatement;
    }

    @Override
    public Integer call() throws Exception
    {
        return this.preparedStatement.executeUpdate();
    }
}
