package net.bartzz.lightlogin.commands;

import net.bartzz.lightlogin.LightLogin;
import org.bukkit.command.CommandSender;

public interface ICommand
{
    default LightLogin getPlugin()
    {
        return LightLogin.getInstance();
    }

    void call(CommandSender sender, CommandContext context);
}
