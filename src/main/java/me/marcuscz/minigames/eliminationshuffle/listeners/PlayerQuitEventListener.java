package me.marcuscz.minigames.eliminationshuffle.listeners;

import me.marcuscz.minigames.core.MinigameEventListener;
import me.marcuscz.minigames.eliminationshuffle.EliminationShuffle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerQuitEventListener extends MinigameEventListener<EliminationShuffle> {


    public PlayerQuitEventListener(EliminationShuffle minigame) {
        super(minigame);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        if (minigame.getPlayerManager().isMinigamePlayer(uuid)) {
            minigame.getPlayerManager().getPlayers().remove(uuid);
            minigame.checkIfAllCompleted();
        }

        if (minigame.getBossBar() != null) {
            minigame.getBossBar().removePlayer(e.getPlayer());
        }
    }
}
