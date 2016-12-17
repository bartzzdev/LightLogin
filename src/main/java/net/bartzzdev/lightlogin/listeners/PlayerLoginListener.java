package net.bartzzdev.lightlogin.listeners;

import net.bartzzdev.lightlogin.LightLogin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {

    private final LightLogin plugin;

    public PlayerLoginListener(LightLogin lightLogin) {
        this.plugin = lightLogin;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        String playerName = event.getPlayer().getName();
        this.plugin.getPostRequest().addNames(playerName);
    }
}
