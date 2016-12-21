package net.bartzzdev.lightlogin.commands;

import com.google.common.collect.Lists;
import net.bartzzdev.lightlogin.LightLogin;
import net.bartzzdev.lightlogin.api.data.ObjectInitializer;
import net.bartzzdev.lightlogin.enums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class CommandContext implements ObjectInitializer<Boolean> {

    private final LightLogin lightLogin = LightLogin.getInstance();
    private final CommandExecutor executor;
    private String name;
    private String[] args;
    private List<String> params;
    private CommandMap commandMap;

    public CommandContext(CommandExecutor executor) {
        this.executor = executor;
    }

    private CommandMap getCommandMap() {
        if (this.commandMap == null) {
            try {
                Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                field.setAccessible(true);
                this.commandMap = (CommandMap) field.get(Bukkit.getServer().getPluginManager());
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return this.commandMap;
    }


    @Override
    public Boolean register() {
        this.getCommandMap();
        Method[] methods = this.executor.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(CommandInfo.class)) {
                return false;
            }

            CommandInfo commandInfo = method.getAnnotation(CommandInfo.class);
            this.name = commandInfo.name();
            new Command(this.name, commandInfo.description(), commandInfo.usage(), Arrays.asList(commandInfo.aliases())) {
                @Override
                public boolean execute(CommandSender commandSender, String s, String[] strings) {
                    args = strings;
                    if (!commandSender.hasPermission(commandInfo.permission())) {
                        lightLogin.sendPrefixedMessage(commandSender, Messages.GLOBAL_NOPERMISSION.getMessage());
                        return true;
                    }

                    if (commandInfo.playerOnly()) {
                        if (!(commandSender instanceof Player)) {
                            lightLogin.sendPrefixedLog(Level.SEVERE, "This command can be performed only by player.");
                            return true;
                        }
                    }
                    return false;
                }
            }.register(this.commandMap);
        }

        this.params = Lists.newArrayList(this.args);
        return true;
    }

    public String getParam(int index, String def) {
        if (this.checkIndex(index)) {
            return def;
        }

        return this.params.get(index);
    }

    public String getParam(int index) {
        return this.getParam(index, null);
    }

    public int getParamInt(int index, int def) {
        if (this.checkIndex(index)) {
            return def;
        }

        int toReturn = Integer.parseInt(this.params.get(index));
        return toReturn;
    }

    public int getParamInt(int index) {
        return this.getParamInt(index, 0);
    }

    public boolean getParamBoolean(int index, boolean def) {
        if (this.checkIndex(index)) {
            return def;
        }

        return this.parseBoolean(this.params.get(index), def);
    }

    public boolean getParamBoolean(int index) {
        return this.getParamBoolean(index, false);
    }

    public int getParamsSize() {
        return this.params.size();
    }

    private boolean parseBoolean(String text, boolean def) {
        switch (text.toLowerCase()) {
            case "yes":
            case "enable":
            case "enabled":
            case "on":
                return Boolean.TRUE;
            case "off":
            case "disabled":
            case "disable":
            case "no":
                return Boolean.FALSE;
        }
        return def;
    }

    private boolean checkIndex(int index) {
        if (index >= this.params.size()) {
            return true;
        }

        return false;
    }
}
