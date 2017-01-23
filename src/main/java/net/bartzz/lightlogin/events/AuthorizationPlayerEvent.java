package net.bartzz.lightlogin.events;

import net.bartzz.lightlogin.api.players.Account;
import net.bartzz.lightlogin.api.players.LightPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AuthorizationPlayerEvent extends Event implements Cancellable
{
    private static HandlerList handlerList;
    private LightPlayer player;
    private boolean cancelled;

    public AuthorizationPlayerEvent(LightPlayer lPlayer)
    {
        this.player = lPlayer;
    }

    public LightPlayer getPlayer()
    {
        return this.player;
    }

    public boolean isPremium()
    {
        return this.player.getAccountType() == Account.PREMIUM;
    }

    @Override
    public HandlerList getHandlers()
    {
        return handlerList;
    }

    public static HandlerList getHandlerList()
    {
        return handlerList;
    }

    @Override
    public boolean isCancelled()
    {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean b)
    {
        this.cancelled = b;
    }
}
