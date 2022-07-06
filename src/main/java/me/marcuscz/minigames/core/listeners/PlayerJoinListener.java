package me.marcuscz.minigames.core.listeners;

import me.marcuscz.minigames.core.Core;
import me.marcuscz.minigames.core.CoreMessages;
import me.marcuscz.minigames.api.Minigame;
import me.marcuscz.minigames.api.MinigamePlayer;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        if (Core.getMinigameManager().isMinigameActive()) {
            Minigame<?,?> minigame = Core.getMinigameManager().getActiveMinigame();
            minigame.onLatePlayerJoin(e.getPlayer());
        }

        if (MinigamePlayer.canBeRegistered(e.getPlayer())) {
            if (Core.getMinigameManager().isMinigameActive()) {
                e.getPlayer().setGameMode(GameMode.SPECTATOR);
                Core.getMessageManager().sendMessage(CoreMessages.JOINED_AS_SPECTATOR, e.getPlayer());
            } else {
                if (Core.getInstance().getCoreOptions().lobby != null) {
                    e.getPlayer().teleport(Core.getInstance().getCoreOptions().lobby);
                    e.getPlayer().setGameMode(GameMode.ADVENTURE);
                } else {
                    e.getPlayer().sendMessage("§4Global lobby location is not set! Please set the lobby location using /game core setlobby, and restart your server.");
                }
            }
        }
    }

}
