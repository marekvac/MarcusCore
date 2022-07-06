package me.marcuscz.minigames.eliminationshuffle.listeners;

import me.marcuscz.minigames.core.MinigameEventListener;
import me.marcuscz.minigames.eliminationshuffle.EliminationPlayer;
import me.marcuscz.minigames.eliminationshuffle.EliminationShuffle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractEventListener extends MinigameEventListener<EliminationShuffle> {

    public PlayerInteractEventListener(EliminationShuffle minigame) {
        super(minigame);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent e) {

        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (e.getClickedBlock() == null) {
            System.out.println("block null");
            return;
        }

        if (!e.getClickedBlock().getLocation().equals(minigame.getBeacon())) {
            return;
        }

        e.setCancelled(true);

        if (minigame.getPlayerManager().isMinigamePlayer(e.getPlayer().getUniqueId())) {
            EliminationPlayer player = minigame.getPlayerManager().getPlayer(e.getPlayer().getUniqueId());
            if (player.checkCompleted()) {
                // broadcast completed
                if (!minigame.checkIfAllCompleted() && minigame.getItems().peek() != null) {
                    player.peekItem(minigame.getItems().peek());
                }
            }
        }

    }

}
