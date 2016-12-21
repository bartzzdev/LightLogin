package net.bartzzdev.lightlogin.commands;

import org.bukkit.command.CommandSender;

public interface CommandExecutor {

    void call(CommandSender sender, CommandContext context);
}
