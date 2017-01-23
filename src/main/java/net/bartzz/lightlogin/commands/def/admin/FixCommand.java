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
        if (context.getPlayer().prefixedMessageIf(Messages.GLOBAL_PARAMS, p -> context.getParamsLength() > 0))
        {
            return;
        }

        this.getPlugin().getLogger().log(Level.INFO, "Rebuilding cache of the plugin...");
        this.getPlugin().buildCache();
    }
}
