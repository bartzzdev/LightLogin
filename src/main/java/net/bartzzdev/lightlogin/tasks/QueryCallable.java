package net.bartzzdev.lightlogin.tasks;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.Callable;

public class QueryCallable implements Callable<ResultSet> {

    private PreparedStatement preparedStatement;

    public QueryCallable(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    @Override
    public ResultSet call() throws Exception {
        return preparedStatement.executeQuery();
    }
}
