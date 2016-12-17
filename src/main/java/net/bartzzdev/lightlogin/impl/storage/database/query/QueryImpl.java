package net.bartzzdev.lightlogin.impl.storage.database.query;

import net.bartzzdev.lightlogin.api.storage.database.query.Query;
import net.bartzzdev.lightlogin.api.thread.Executor;
import net.bartzzdev.lightlogin.api.thread.ExecutorInitializer;
import net.bartzzdev.lightlogin.tasks.QueryCallable;
import net.bartzzdev.lightlogin.tasks.UpdateCallable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class QueryImpl implements Query {

    private PreparedStatement preparedStatement;

    public QueryImpl(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    @Override
    public int executeUpdate() {
        Executor<Integer> updateExecutor = new ExecutorInitializer(new UpdateCallable(this.preparedStatement));
        return updateExecutor.call();
    }

    @Override
    public ResultSet executeQuery() {
        Executor<ResultSet> queryExecutor = new ExecutorInitializer(new QueryCallable(this.preparedStatement));
        return queryExecutor.call();
    }
}
