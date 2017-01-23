package net.bartzz.lightlogin.commands.def.admin;

import net.bartzz.lightlogin.api.files.enums.Messages;
import net.bartzz.lightlogin.api.players.LightPlayer;
import net.bartzz.lightlogin.commands.CommandContext;
import net.bartzz.lightlogin.commands.CommandInfo;
import net.bartzz.lightlogin.commands.ICommand;
import org.bukkit.command.CommandSender;

public class AccountCommand implements ICommand
{
    @CommandInfo(name = "account",
    permission = "lightlogin.command.account",
    usage = "/account <player>",
    description = "Info about any player account",
    aliases = { "acc" })
    public void call(CommandSender sender, CommandContext context)
    {
        if (context.getPlayer().coloredPrefixedMessageIf(Messages.GLOBAL_PARAMS, p -> context.getParamsLength() != 1))
        {
            return;
        }

        String playerName = context.getParam(0);
        LightPlayer paramPlayer = this.getPlugin().getPlayerManager().get(playerName);

        if (context.getPlayer().coloredMessageIf(Messages.ACCOUNTCOMMAND_PLAYERNOTFOUND.replace(new String[] {"<%name>"}, new String[] {playerName}),
                p -> paramPlayer == null))
        {
            return;
        }

        context.getPlayer().coloredMessage(Messages.ACCOUNTCOMMAND_TRIGGER.replace(
                new String[] {"<%player>", "<%account>", "<%playerId>"},
                new String[] {playerName, paramPlayer.getAccountType().toString().toLowerCase(), paramPlayer.getPlayerId().toString()}
        ));
    }
}
