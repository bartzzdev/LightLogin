package net.bartzz.lightlogin.utils;

import net.bartzz.lightlogin.api.threads.ExecutorInitializer;
import net.bartzz.lightlogin.callables.ContentCallable;

public class IOUtils
{
    private IOUtils()
    {
    }

    public static String getContent(String url)
    {
        return new ExecutorInitializer<String>().newExecutor(new ContentCallable(url)).execute();
    }
}
