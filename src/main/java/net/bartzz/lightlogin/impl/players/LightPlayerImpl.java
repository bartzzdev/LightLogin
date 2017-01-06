package net.bartzz.lightlogin.impl.players;

import net.bartzz.lightlogin.LightLogin;
import net.bartzz.lightlogin.api.data.DataInitializer;
import net.bartzz.lightlogin.api.files.enums.Messages;
import net.bartzz.lightlogin.api.players.Account;
import net.bartzz.lightlogin.api.players.LightPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

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

    @Override
    public void sendPrefixedMessage(Messages message)
    {
        if (this.bukkitPlayer == null)
        {
            return;
        }

        this.bukkitPlayer.sendMessage(ChatColor.GRAY + "[" + ChatColor.DARK_AQUA + "LightLogin" + ChatColor.GRAY + "] " + LightLogin.getInstance().getMessages().get(message));
    }
}
