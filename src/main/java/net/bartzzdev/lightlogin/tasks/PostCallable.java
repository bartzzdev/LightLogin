package net.bartzzdev.lightlogin.tasks;

import net.bartzzdev.lightlogin.api.data.ObjectInitializer;
import org.json.simple.JSONArray;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

public class PostCallable implements Callable<String>, ObjectInitializer<PostCallable> {

    private HttpURLConnection connection;
    private String url;
    private String[] playerNames;
    private StringBuilder response;
    private boolean success;

    public PostCallable(String url) {
        this.url = url;
    }

    @Override
    public String call() throws Exception {
        this.connection.setRequestProperty("Content-Type", "application/json");
        this.connection.setRequestMethod("POST");
        JSONArray array = new JSONArray();
        for (String name : this.playerNames) array.add(name);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(this.connection.getOutputStream()));
        writer.write(array.toString());
        writer.flush();

        if (this.connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            this.success = true;
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
            reader.lines().map(string -> string + "\n").forEach(this.response::append);
        } else this.success = false;
        return this.response.toString();
    }

    @Override
    public PostCallable register() {
        this.playerNames = new String[100];
        this.response = new StringBuilder();
        try {
            this.connection = (HttpURLConnection) new URL(this.url).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void addNames(String... names) {
        for (int i = 0; i < names.length; i++) {
            this.playerNames[i] = names[i];
        }
    }

    public boolean success() {
        return this.success;
    }
}
