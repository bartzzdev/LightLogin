package net.bartzz.lightlogin;

import ch.njol.skript.Skript;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import net.bartzz.lightlogin.api.LightLoginAPI;
import net.bartzz.lightlogin.api.files.LightConfiguration;
import net.bartzz.lightlogin.api.files.LightMessages;
import net.bartzz.lightlogin.api.files.enums.Configuration;
import net.bartzz.lightlogin.api.players.LightPlayerManager;
import net.bartzz.lightlogin.api.storage.Storage;
import net.bartzz.lightlogin.api.storage.database.Database;
import net.bartzz.lightlogin.api.supports.Supports;
import net.bartzz.lightlogin.api.supports.skript.conditions.CrackedCondition;
import net.bartzz.lightlogin.api.supports.skript.conditions.PremiumCondition;
import net.bartzz.lightlogin.commands.CommandContext;
import net.bartzz.lightlogin.commands.def.admin.AccountCommand;
import net.bartzz.lightlogin.commands.def.admin.FixCommand;
import net.bartzz.lightlogin.impl.files.LightConfigurationImpl;
import net.bartzz.lightlogin.impl.files.LightMessagesImpl;
import net.bartzz.lightlogin.impl.players.LightPlayerManagerImpl;
import net.bartzz.lightlogin.impl.storage.FileStorage;
import net.bartzz.lightlogin.impl.storage.MysqlStorage;
import net.bartzz.lightlogin.impl.storage.database.DatabaseImpl;
import net.bartzz.lightlogin.nms.LightServerConnection;
import net.minecraft.server.v1_11_R1.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
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

    private ProtocolManager protocolManager;

    @Override
    public void onLoad()
    {
        LightLogin.lightLogin = this;
        this.buildCache();
    }

    @Override
    public void onDisable()
    {
        try
        {
            this.storage.saveAll();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void buildCache()
    {
        long start = System.currentTimeMillis();

        this.logger = Bukkit.getLogger();

        this.protocolManager = ProtocolLibrary.getProtocolManager();

        boolean success = false;
        if (!Supports.PROTOCOLLIB.isSupported())
        {
            try
            {
                this.logger.log(Level.INFO, "Overriting server connection..");
                Field field = MinecraftServer.class.getDeclaredField("p");
                field.setAccessible(true);
                field.set(MinecraftServer.getServer(), new LightServerConnection(MinecraftServer.getServer()));
                success = true;
            } catch (IllegalAccessException | NoSuchFieldException e)
            {
                e.printStackTrace();
            }
        }

        if (Supports.SKRIPT.isSupported())
        {
            this.logger.log(Level.INFO, "Registering Skript's addon..");
            Skript.registerAddon(this);
            Skript.registerCondition(PremiumCondition.class, "%player% (is|has) premium", "%player% (isn't|is not|hasn't|has not) premium");
            Skript.registerCondition(CrackedCondition.class, "%player% is cracked", "%player% isn't cracked");
        }

        if (success)
        {
            this.logger.log(Level.INFO, "Successfully overrited server connection..");
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
        this.database = new DatabaseImpl(Configuration.MYSQL_HOST.getString("localhost"),
                                                Configuration.MYSQL_USER.getString("root"),
                                                Configuration.MYSQL_PASSWORD.getString(""),
                                                Configuration.MYSQL_DATABASE.getString("lightlogin_database"),
                                                Configuration.MYSQL_PORT.getInt(3306));

        this.logger.log(Level.INFO, "Initializing storage..");
        if (Configuration.STORAGE_MYSQL.getBoolean(true))
        {
            this.storage = new MysqlStorage();
            this.database.connect();
        } else
        {
            this.storage = new FileStorage();
        }

        try
        {
            this.storage.loadAll();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

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

    public ProtocolManager getProtocolManager()
    {
        return this.protocolManager;
    }

    public static LightLogin getInstance()
    {
        return LightLogin.lightLogin;
    }
}
