package net.bartzz.lightlogin.commands.def.admin;

import net.bartzz.lightlogin.api.files.enums.Messages;
import net.bartzz.lightlogin.commands.CommandContext;
import net.bartzz.lightlogin.commands.CommandInfo;
import net.bartzz.lightlogin.commands.ICommand;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;

public class FixCommand implements ICommand
{
    @CommandInfo(name = "fix",
    permission = "lightlogin.command.fix",
    usage = "/fix",
    description = "Fixes the cache",
    aliases = { "rebuild", "newcache" })
    public void call(CommandSender sender, CommandContext context)
    {
        if (context.getParamsLength() != 0)
        {
            sender.sendMessage(Messages.GLOBAL_PARAMS.getMessage());
            return;
        }

        this.getPlugin().getLogger().log(Level.INFO, "Rebuilding cache of the plugin...");
        this.getPlugin().buildCache();
    }
}
