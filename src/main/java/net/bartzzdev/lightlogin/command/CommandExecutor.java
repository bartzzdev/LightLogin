package net.bartzzdev.lightlogin.command;

import org.bukkit.command.CommandSender;

public interface CommandExecutor {

    void call(CommandSender sender, CommandContext context);
}
