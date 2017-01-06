package net.bartzz.lightlogin.api.threads;

import net.bartzz.lightlogin.LightLogin;

import java.util.concurrent.Callable;

public class ExecutorInitializer<T>
{
    private final LightLogin PLUGIN = LightLogin.getInstance();

    public Executor<T> newExecutor(Callable<T> call)
    {
        return new Executor<T>()
        {
            @Override
            public T execute()
            {
                try
                {
                    ExecutorInitializer.this.PLUGIN.getExecutorService().submit(call).get();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }
}
