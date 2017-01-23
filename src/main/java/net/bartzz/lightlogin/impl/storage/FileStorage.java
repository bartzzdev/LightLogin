package net.bartzz.lightlogin.impl.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.bartzz.lightlogin.LightLogin;
import net.bartzz.lightlogin.api.players.LightPlayer;
import net.bartzz.lightlogin.api.storage.Storage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileStorage implements Storage
{
    private LightLogin lightLogin = LightLogin.getInstance();
    private final File DIRECTORY = new File(this.lightLogin.getDataFolder(), "players data");

    @Override
    public void loadAll() throws IOException
    {
        if (!this.DIRECTORY.exists())
        {
            this.DIRECTORY.mkdir();
        }

        for (File file : this.DIRECTORY.listFiles())
        {
            Gson gson = new GsonBuilder().create();
            LightPlayer lPlayer = gson.fromJson(new FileReader(file), LightPlayer.class);
            this.lightLogin.getPlayerManager().getPlayersMap().put(lPlayer.getPlayerId(), lPlayer);
        }
    }

    @Override
    public void saveAll() throws IOException
    {
        if (!this.DIRECTORY.exists())
        {
            this.DIRECTORY.mkdir();
        }

        for (LightPlayer lPlayer : this.lightLogin.getPlayerManager().getPlayersMap().values())
        {
            Gson gson = new GsonBuilder().create();
            gson.toJson(lPlayer, new FileWriter(new File(this.DIRECTORY, lPlayer.getPlayerId().toString() + ".json")));
        }
    }

    @Override
    public Storage getStorage()
    {
        return new FileStorage();
    }
}
