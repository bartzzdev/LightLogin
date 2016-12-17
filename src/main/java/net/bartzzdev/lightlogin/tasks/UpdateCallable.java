package net.bartzzdev.lightlogin.tasks;

import java.sql.PreparedStatement;
import java.util.concurrent.Callable;

public class UpdateCallable implements Callable<Integer> {

    private PreparedStatement preparedStatement;

    public UpdateCallable(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    @Override
    public Integer call() throws Exception {
        return this.preparedStatement.executeUpdate();
    }
}
