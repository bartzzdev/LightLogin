package net.bartzz.lightlogin.commands;

import net.bartzz.lightlogin.LightLogin;
import net.bartzz.lightlogin.api.data.DataInitializer;
import net.bartzz.lightlogin.api.files.enums.Messages;
import net.bartzz.lightlogin.api.players.LightPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class CommandContext implements DataInitializer<CommandContext>
{
    private LightLogin lightLogin = LightLogin.getInstance();

    private ICommand command;
    private CommandMap cmdMap;
    private LightPlayer player;

    private List<String> params = new ArrayList<>();

    public CommandContext(ICommand command)
    {
        this.command = command;
    }

    public LightPlayer getPlayer()
    {
        return this.player;
    }

    public int getParamsLength()
    {
        return this.params.size();
    }

    public String getParam(int index)
    {
        if (index >= this.params.size())
        {
            return "";
        }

        return this.params.get(index);
    }

    public String getParam(int index, String def)
    {
        if (index >= this.params.size())
        {
            return def;
        }

        return this.params.get(index);
    }

    public int getParamInt(int index)
    {
        if (index >= this.params.size())
        {
            return 0;
        }

        return Integer.parseInt(this.params.get(index));
    }

    public int getParamInt(int index, int def)
    {
        if (index >= this.params.size())
        {
            return def;
        }

        return Integer.parseInt(this.params.get(index));
    }

    public boolean getParamBoolean(int index)
    {
        if (index >= this.params.size())
        {
            return false;
        }

        return this.parseBoolean(this.params.get(index), false);
    }

    public boolean getParamBoolean(int index, boolean def)
    {
        if (index >= this.params.size())
        {
            return def;
        }

        return this.parseBoolean(this.params.get(index), def);
    }

    private boolean parseBoolean(String text, boolean def)
    {
        switch (text.toLowerCase())
        {
            case "true":
            case "1":
            case "on":
            case "enabled":
            case "enable":
                return Boolean.TRUE;
            case "false":
            case "0":
            case "off":
            case "disabled":
            case "disable":
                return Boolean.FALSE;
            default:
                return def;
        }
    }

    @Override
    public CommandContext register()
    {
        if (this.cmdMap == null)
        {
            try
            {
                Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                field.setAccessible(true);
                this.cmdMap = ((CommandMap) field.get(Bukkit.getServer().getPluginManager()));
            } catch (IllegalAccessException | NoSuchFieldException e)
            {
                e.printStackTrace();
            }
        }

        Method[] methods = this.command.getClass().getDeclaredMethods();
        for (Method method : methods)
        {
            if (method.isAnnotationPresent(CommandInfo.class))
            {
                CommandInfo info = method.getAnnotation(CommandInfo.class);
                Command bukkitCommand = new Command(info.name(), info.description(), info.usage(), Arrays.asList(info.aliases()))
                {
                    @Override
                    public boolean execute(CommandSender commandSender, String s, String[] strings)
                    {
                        CommandContext.this.params.clear();
                        for (int i = 0; i < strings.length; i++)
                        {
                            CommandContext.this.params.add(i, strings[i]);
                        }

                        if (info.playerOnly())
                        {
                            if (!(commandSender instanceof Player))
                            {
                                Bukkit.getLogger().log(Level.SEVERE, Messages.GLOBAL_NOTPLAYER.getRawMessage());
                                return true;
                            }
                        }

                        Player player = (Player) commandSender;
                        LightPlayer lPlayer = CommandContext.this.lightLogin.getPlayerManager().get(player.getUniqueId());
                        CommandContext.this.player = lPlayer;

                        if (lPlayer == null)
                        {
                            return true;
                        }

                        if (!commandSender.hasPermission(info.permission()))
                        {
                            lPlayer.sendPrefixedMessage(Messages.GLOBAL_NOPERMISSION);
                            return true;
                        }

                        CommandContext.this.command.call(commandSender, CommandContext.this);
                        return false;
                    }
                };
                this.cmdMap.register(this.lightLogin.getName(), bukkitCommand);
            }
        }
        return this;
    }
}
