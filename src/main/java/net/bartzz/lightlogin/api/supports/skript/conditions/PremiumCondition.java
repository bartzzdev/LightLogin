package net.bartzz.lightlogin.api.supports.skript.conditions;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Checker;
import ch.njol.util.Kleenean;
import net.bartzz.lightlogin.LightLogin;
import net.bartzz.lightlogin.api.players.Account;
import net.bartzz.lightlogin.api.players.LightPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class PremiumCondition extends Condition
{
    private LightLogin lightLogin = LightLogin.getInstance();

    private Expression<Player> player;
    private LightPlayer lightPlayer;
    private boolean premium;

    @Override
    public boolean check(Event event)
    {
        return player.check(event, new Checker<Player>()
        {
            @Override
            public boolean check(Player player)
            {
                LightPlayer lightPlayer = PremiumCondition.this.lightLogin.getPlayerManager().get(player.getUniqueId());
                if (lightPlayer == null)
                {
                    return false;
                }

                if (!lightPlayer.getAccountType().equals(Account.PREMIUM))
                {
                    return false;
                }

                return true;
            }
        }, isNegated());
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult)
    {
        this.player = (Expression<Player>) expressions[0];
        return true;
    }

    @Override
    public String toString(Event event, boolean b)
    {
        return this.getClass().getName();
    }
}
