package net.bartzz.lightlogin.commands.def.admin;

import net.bartzz.lightlogin.api.files.enums.Messages;
import net.bartzz.lightlogin.api.players.LightPlayer;
import net.bartzz.lightlogin.commands.CommandContext;
import net.bartzz.lightlogin.commands.ICommand;
import org.bukkit.command.CommandSender;

public class AccountCommand implements ICommand
{
    @Override
    public void call(CommandSender sender, CommandContext context)
    {
        if (context.getParamsLength() != 1)
        {
            context.getPlayer().sendPrefixedMessage(Messages.GLOBAL_PARAMS);
            return;
        }

        String playerName = context.getParam(0);
        LightPlayer paramPlayer = this.getPlugin().getPlayerManager().get(playerName);

        if (paramPlayer == null)
        {
            sender.sendMessage(Messages.ACCOUNTCOMMAND_PLAYERNOTFOUND.getMessageReplaced(new String[]{"<%name>"}, new String[]{playerName}));
            return;
        }

        sender.sendMessage(Messages.ACCOUNTCOMMAND_TRIGGER.getMessageReplaced(
                new String[] {"<%player>", "<%account>", "<%playerId>"},
                new String[] {playerName, paramPlayer.getAccountType().toString().toUpperCase(), paramPlayer.getPlayerId().toString()}
        ));
    }
}
