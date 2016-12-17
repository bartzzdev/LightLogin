package net.bartzzdev.lightlogin.api.thread;

import java.util.concurrent.Callable;

public class ExecutorInitializer implements Executor {

    private Callable callable;

    public ExecutorInitializer(Callable callable) {
        this.callable = callable;
    }

    @Override
    public Object call() {
        try {
            return this.callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
