package net.bartzz.lightlogin.callables;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

public class ContentCallable implements Callable<String>
{
    private final String URL_STRING;
    private StringBuilder stringBuilder = new StringBuilder();

    public ContentCallable(String url)
    {
        this.URL_STRING = url;
    }

    @Override
    public String call() throws Exception
    {
        HttpURLConnection connection = (HttpURLConnection) new URL(this.URL_STRING).openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        bufferedReader.lines().map(string -> string += "\n").forEach(this.stringBuilder::append);
        return this.stringBuilder.toString();
    }
}
