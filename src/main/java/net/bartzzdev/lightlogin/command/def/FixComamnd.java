package net.bartzzdev.lightlogin.command.def;

import net.bartzzdev.lightlogin.LightLogin;
import net.bartzzdev.lightlogin.command.CommandContext;
import net.bartzzdev.lightlogin.command.CommandExecutor;
import net.bartzzdev.lightlogin.command.CommandInfo;
import net.bartzzdev.lightlogin.enums.Messages;
import org.bukkit.command.CommandSender;

public class FixComamnd implements CommandExecutor {

    private LightLogin lightLogin = LightLogin.getInstance();

    @CommandInfo(value = "fix", permission = "lightlogin.command.fix", description = "Fixes some errors in cache.", aliases = { "" }, usage = "/fix")
    @Override
    public void call(CommandSender sender, CommandContext context) {
        if (context.getArgs().length != 0) {
            this.lightLogin.sendPrefixedMessage(sender, Messages.GLOBAL_INVALIDARGS.getMessage());
            return;
        }

        this.lightLogin.buildCache();
    }
}
