package net.bartzz.lightlogin.callables;

import net.bartzz.lightlogin.LightLogin;
import net.bartzz.lightlogin.api.players.LightPlayerManager;
import org.json.simple.JSONArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

public class PostCallable implements Callable<String>
{
    private boolean success;
    private HttpURLConnection connection;
    private StringBuilder response = new StringBuilder();
    private LightPlayerManager playerManager = LightLogin.getInstance().getPlayerManager();

    @Override
    public String call() throws Exception
    {
        this.connection = (HttpURLConnection) new URL("https://api.mojang.com/profiles/minecraft").openConnection();
        this.connection.setRequestProperty("Content-Type", "application/json");
        this.connection.setRequestMethod("POST");
        this.connection.setDoInput(true);
        this.connection.setDoOutput(true);
        JSONArray array = new JSONArray();
        for (String name : this.playerManager.getNamesAwaiting())
        {
            array.add(name);
        }

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(this.connection.getOutputStream()));
        writer.write(array.toString());
        writer.flush();

        if (this.connection.getResponseCode() == HttpURLConnection.HTTP_OK)
        {
            this.success = true;
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
            reader.lines().map(str -> str + '\n').forEach(this.response::append);
        } else this.success = false;

        return this.response.toString();
    }

    public boolean success()
    {
        return this.success;
    }
}
