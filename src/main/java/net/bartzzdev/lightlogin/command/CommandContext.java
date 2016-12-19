package net.bartzzdev.lightlogin.command;

import net.bartzzdev.lightlogin.LightLogin;
import net.bartzzdev.lightlogin.api.data.ObjectInitializer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.logging.Level;

public class CommandContext implements ObjectInitializer<CommandInfo> {

    private CommandExecutor executor;
    private String name, description, permission, usage;
    private String[] aliases, args;
    private boolean onlyPlayer;
    private CommandMap commandMap = this.getCommandMap();

    public CommandContext(CommandExecutor executor) {
        this.executor = executor;
    }

    private void initCommand(Command command) {
        command.setName(this.name);
        command.setAliases(Arrays.asList(this.aliases));
        command.setPermission(this.permission);
        command.setDescription(this.description);
        command.setUsage(this.usage);
        command.setPermissionMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3LightLogin&7] You don't have permission to &3perform &7this command!"));
    }

    private CommandMap getCommandMap() {
        if (this.commandMap == null) {
            try {
                Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                field.setAccessible(true);
                this.commandMap = (CommandMap) field.get(Bukkit.getServer().getPluginManager());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return this.commandMap;
        }
        return this.commandMap;
    }

    @Override
    public CommandInfo register() {
        CommandInfo commandInfo = null;
        if (this.executor.getClass().isAnnotationPresent(CommandInfo.class)) {
            commandInfo = this.executor.getClass().getAnnotation(CommandInfo.class);
            this.name = commandInfo.value();
            this.description = commandInfo.description();
            this.permission = commandInfo.permission();
            this.usage = commandInfo.usage();
            this.aliases = commandInfo.aliases();
            this.onlyPlayer = commandInfo.onlyPlayer();
            Command command = new Command(this.name) {
                @Override
                public boolean execute(CommandSender commandSender, String s, String[] strings) {
                    args = strings;
                    if (!commandSender.hasPermission(permission)) {
                        commandSender.sendMessage(this.getPermissionMessage());
                        return false;
                    }

                    if (onlyPlayer) {
                        if (!(commandSender instanceof Player)) {
                            LightLogin.getInstance().sendPrefixedLog(Level.SEVERE, "Command ~" + this.getName() + " can be executed only by player.");
                            return false;
                        }
                    }

                    executor.call(commandSender, CommandContext.this);
                    return false;
                }
            };

            this.initCommand(command);
            this.commandMap.register("lightLogin", command);
        }
        return commandInfo;
    }

    public String getName() {
        return this.name;
    }

    public CommandExecutor getExecutor() {
        return this.executor;
    }

    public String[] getArgs() {
        return this.args;
    }
}
