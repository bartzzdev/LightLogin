package net.bartzzdev.lightlogin.commands.def;

import net.bartzzdev.lightlogin.LightLogin;
import net.bartzzdev.lightlogin.commands.CommandContext;
import net.bartzzdev.lightlogin.commands.CommandExecutor;
import net.bartzzdev.lightlogin.enums.Messages;
import org.bukkit.command.CommandSender;

public class FixCommand implements CommandExecutor {

    private final LightLogin lightLogin;

    public FixCommand(LightLogin lightLogin) {
        this.lightLogin = lightLogin;
    }

    @Override
    public void call(CommandSender sender, CommandContext context) {
        if (context.getParamsSize() != 0) {
            this.lightLogin.sendPrefixedMessage(sender, Messages.GLOBAL_INVALIDARGS.getMessage());
            return;
        }

        this.lightLogin.buildCache();
    }
}
