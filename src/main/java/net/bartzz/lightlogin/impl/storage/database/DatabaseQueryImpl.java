package net.bartzz.lightlogin.impl.storage.database;

import net.bartzz.lightlogin.api.storage.database.DatabaseQuery;
import net.bartzz.lightlogin.api.threads.Executor;
import net.bartzz.lightlogin.api.threads.ExecutorInitializer;
import net.bartzz.lightlogin.callables.ExecuteQueryCallable;
import net.bartzz.lightlogin.callables.ExecuteUpdateCallable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseQueryImpl implements DatabaseQuery
{
    private PreparedStatement preparedStatement;

    public DatabaseQueryImpl(PreparedStatement preparedStatement)
    {
        this.preparedStatement = preparedStatement;
    }

    @Override
    public int executeUpdate()
    {
        ExecuteUpdateCallable callable = new ExecuteUpdateCallable(this.preparedStatement);
        Executor<Integer> executor = new ExecutorInitializer<Integer>().newExecutor(callable);
        return executor.execute();
    }

    @Override
    public ResultSet executeQuery()
    {
        ExecuteQueryCallable callable = new ExecuteQueryCallable(this.preparedStatement);
        Executor<ResultSet> executor = new ExecutorInitializer<ResultSet>().newExecutor(callable);
        return executor.execute();
    }
}
