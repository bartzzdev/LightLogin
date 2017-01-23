package net.bartzz.lightlogin.impl.players;

import net.bartzz.lightlogin.api.data.DataInitializer;
import net.bartzz.lightlogin.api.files.enums.Messages;
import net.bartzz.lightlogin.api.players.Account;
import net.bartzz.lightlogin.api.players.LightPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.Predicate;

public class LightPlayerImpl implements LightPlayer, DataInitializer<LightPlayer>
{
    private Player bukkitPlayer;

    private final UUID playerId;
    private String playerName;
    private Account accountType;

    public LightPlayerImpl(UUID playerId, String playerName)
    {
        this.playerId = playerId;
        this.playerName = playerName;
    }

    @Override
    public LightPlayer register()
    {
        this.bukkitPlayer = Bukkit.getPlayer(this.playerId);
        return this;
    }

    @Override
    public void prefixedMessage(Messages message)
    {
        if (this.bukkitPlayer == null)
        {
            return;
        }

        this.bukkitPlayer.sendMessage(new StringBuilder().append(this.PREFIX).append(' ').append(message.getRawMessage()).toString());
    }

    @Override
    public boolean prefixedMessageIf(Messages message, Predicate<LightPlayer> predicate)
    {
        if (predicate.test(this))
        {
            this.prefixedMessage(message);
            return true;
        }
        return false;
    }

    @Override
    public void coloredMessage(Messages message)
    {
        if (this.bukkitPlayer == null)
        {
            return;
        }

        this.bukkitPlayer.sendMessage(message.getMessage());
    }

    @Override
    public boolean coloredMessageIf(Messages message, Predicate<LightPlayer> predicate)
    {
        if (predicate.test(this))
        {
            this.coloredMessage(message);
            return true;
        }
        return false;
    }

    @Override
    public void coloredPrefixedMessage(Messages message)
    {
        if (this.bukkitPlayer == null)
        {
            return;
        }

        this.bukkitPlayer.sendMessage(new StringBuilder().append(this.PREFIX).append(' ').append(message.getMessage()).toString());
    }

    @Override
    public boolean coloredPrefixedMessageIf(Messages message, Predicate<LightPlayer> predicate)
    {
        if (predicate.test(this))
        {
            this.coloredPrefixedMessage(message);
            return true;
        }
        return false;
    }

    @Override
    public UUID getPlayerId()
    {
        return this.playerId;
    }

    @Override
    public String getPlayerName()
    {
        return this.playerName;
    }

    @Override
    public Account getAccountType()
    {
        return this.accountType == null ? Account.NOT_AUTHORIZED : this.accountType;
    }

    @Override
    public void setAccountType(Account accountType)
    {
        this.accountType = accountType;
    }
}
