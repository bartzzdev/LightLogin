package net.bartzz.lightlogin;

import net.bartzz.lightlogin.api.LightLoginAPI;
import net.bartzz.lightlogin.api.files.LightConfiguration;
import net.bartzz.lightlogin.api.files.LightMessages;
import net.bartzz.lightlogin.api.players.LightPlayerManager;
import net.bartzz.lightlogin.api.storage.Storage;
import net.bartzz.lightlogin.api.storage.database.Database;
import net.bartzz.lightlogin.commands.CommandContext;
import net.bartzz.lightlogin.commands.def.admin.AccountCommand;
import net.bartzz.lightlogin.commands.def.admin.FixCommand;
import net.bartzz.lightlogin.impl.files.LightConfigurationImpl;
import net.bartzz.lightlogin.impl.files.LightMessagesImpl;
import net.bartzz.lightlogin.impl.players.LightPlayerManagerImpl;
import net.bartzz.lightlogin.impl.storage.MysqlStorage;
import net.bartzz.lightlogin.impl.storage.database.DatabaseImpl;
import net.bartzz.lightlogin.nms.LightServerConnection;
import net.minecraft.server.v1_11_R1.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class LightLogin extends JavaPlugin implements LightLoginAPI
{
    private static LightLogin lightLogin;

    private Logger logger;

    private LightPlayerManager playerManager;

    private LightMessages lightMessages;
    private LightConfiguration lightConfiguration;

    private ExecutorService executorService;

    private Database database;
    private Storage storage;

    @Override
    public void onLoad()
    {
        LightLogin.lightLogin = this;
        this.buildCache();
    }

    @Override
    public void onDisable()
    {
        /*
        this.storage.saveAll();
        */
    }

    @Override
    public void buildCache()
    {
        long start = System.currentTimeMillis();

        this.logger = Bukkit.getLogger();

        this.logger.log(Level.INFO, "Overriting server connection..");
        boolean success = false;
        try
        {
            Field field = MinecraftServer.class.getDeclaredField("p");
            field.setAccessible(true);
            field.set(MinecraftServer.getServer(), new LightServerConnection(MinecraftServer.getServer()));
            success = true;
        } catch (IllegalAccessException | NoSuchFieldException e)
        {
            e.printStackTrace();
        }

        if (success)
        {
            this.logger.log(Level.INFO, "Successfully overrited server connection..");
        } else
        {
            this.logger.log(Level.SEVERE, "Failed to overrite server connection..");
        }

        this.logger.log(Level.INFO, "Initializing managers..");
        this.playerManager = new LightPlayerManagerImpl();

        this.logger.log(Level.INFO, "Loading messages..");
        this.lightMessages = new LightMessagesImpl();
        this.lightMessages.load();

        this.logger.log(Level.INFO, "Loading configuration..");
        this.lightConfiguration = new LightConfigurationImpl();
        this.lightConfiguration.load();

        this.logger.log(Level.INFO, "Initializing thread pool..");
        this.executorService = Executors.newCachedThreadPool();

        this.logger.log(Level.INFO, "Preparing database..");
        this.database = new DatabaseImpl("", "", "", "", 0);
        /*
        this.database.connect();
        */

        this.logger.log(Level.INFO, "Initializing storage..");
        this.storage = new MysqlStorage();
        /*
        this.storage.loadAll();
        */

        this.logger.log(Level.INFO, "Cache has built (" + (System.currentTimeMillis() - start) + "ms).");

    }

    @Override
    public LightPlayerManager getPlayerManager()
    {
        return this.playerManager;
    }

    @Override
    public LightMessages getMessages()
    {
        return this.lightMessages;
    }

    @Override
    public LightConfiguration getConfiguration()
    {
        return this.lightConfiguration;
    }

    @Override
    public Database getLightDatabase()
    {
        return this.database;
    }

    private void loadCommands()
    {
        new CommandContext(new FixCommand()).register();
        new CommandContext(new AccountCommand()).register();
    }

    public ExecutorService getExecutorService()
    {
        return this.executorService;
    }

    public static LightLogin getInstance()
    {
        return LightLogin.lightLogin;
    }
}
