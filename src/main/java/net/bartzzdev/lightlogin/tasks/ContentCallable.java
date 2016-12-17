package net.bartzzdev.lightlogin.tasks;

import net.bartzzdev.lightlogin.api.data.ObjectInitializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

public class ContentCallable implements Callable<String>, ObjectInitializer<ContentCallable> {

    private String url;
    private HttpURLConnection connection;
    private StringBuilder content;

    public ContentCallable(String url) {
        this.url = url;
    }

    @Override
    public String call() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
        reader.lines().map(string -> string + "\n").forEach(this.content::append);
        return this.content.toString();
    }

    public ContentCallable register() {
        try {
            this.connection = (HttpURLConnection) new URL(this.url).openConnection();
            this.content = new StringBuilder();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }
}
