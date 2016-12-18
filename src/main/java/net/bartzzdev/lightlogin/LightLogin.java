package net.bartzzdev.lightlogin;

import net.bartzzdev.lightlogin.api.LightLoginAPI;
import net.bartzzdev.lightlogin.api.data.ObjectInitializer;
import net.bartzzdev.lightlogin.api.players.LightPlayerManager;
import net.bartzzdev.lightlogin.api.storage.database.Database;
import net.bartzzdev.lightlogin.api.yaml.LightConfiguration;
import net.bartzzdev.lightlogin.enums.Configuration;
import net.bartzzdev.lightlogin.enums.Storage;
import net.bartzzdev.lightlogin.impl.players.LightPlayerManagerImpl;
import net.bartzzdev.lightlogin.impl.storage.database.DatabaseImpl;
import net.bartzzdev.lightlogin.tasks.PostCallable;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LightLogin extends JavaPlugin implements LightLoginAPI {

    private static LightLogin lightLogin;
    private ObjectInitializer[] managers;
    private ExecutorService executorService;
    private Future<String> result;
    private Database database;
    private PostCallable post;
    private LightConfiguration configuration;

    public LightLogin() {
        lightLogin = this;
    }

    public void onLoad() {
        this.buildCache();

        if (this.getServer().getOnlineMode()) {
            this.sendPrefixedMessage(Bukkit.getConsoleSender(), "&3Server must be in offline mode.");
            this.setEnabled(false);
        }
    }

    public void onEnable() {
        this.saveDefaultConfig();
    }

    public static LightLogin getInstance() {
        return lightLogin;
    }

    @Override
    public void buildCache() {
        this.managers = new ObjectInitializer[5];
        this.executorService = Executors.newCachedThreadPool();

        this.managers[0] = new LightPlayerManagerImpl();
        this.managers[0].register();

        this.post = new PostCallable("https://api.mojang.com/profiles/minecraft").register();
        this.result = this.executorService.submit(this.post);

        this.configuration = new LightConfiguration();
        this.configuration.load();

        this.database = new DatabaseImpl(
                Configuration.STORAGE_MYSQL_HOST.getString(),
                Configuration.STORAGE_MYSQL_USER.getString(),
                Configuration.STORAGE_MYSQL_PASSWORD.getString(),
                Configuration.STORAGE_MYSQL_DATABASE.getString(),
                Configuration.STORAGE_MYSQL_PORT.getInteger());
    }

    @Override
    public Future<String> getResult() {
        return this.result;
    }

    @Override
    public Database getLoginDatabase() {
        return this.database;
    }

    @Override
    public void sendPrefixedMessage(CommandSender commandSender, String message) {
        if (message.isEmpty()) return;
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&cLightLogin&7] " + message));
    }

    @Override
    public LightPlayerManager getPlayerManager() {
        return (LightPlayerManager) this.managers[0];
    }

    @Override
    public Storage getStorageType() {
        if (Configuration.STORAGE_MYSQL.getBoolean()) {
            return Storage.MYSQL;
        } else return Storage.FLAT;
    }

    @Override
    public LightConfiguration getConfiguration() {
        return this.configuration;
    }

    public PostCallable getPostRequest() {
        return this.post;
    }
}
