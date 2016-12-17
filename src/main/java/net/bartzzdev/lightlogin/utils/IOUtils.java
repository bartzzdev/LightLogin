package net.bartzzdev.lightlogin.utils;

import net.bartzzdev.lightlogin.api.thread.ExecutorInitializer;
import net.bartzzdev.lightlogin.api.thread.Executor;
import net.bartzzdev.lightlogin.tasks.ContentCallable;

public class IOUtils {

    public static String getContent(String url) {
        Executor<String> contentExecutor = new ExecutorInitializer(new ContentCallable(url).register());
        return contentExecutor.call();
    }
}
