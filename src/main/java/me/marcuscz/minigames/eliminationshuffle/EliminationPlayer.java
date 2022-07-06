package me.marcuscz.minigames.eliminationshuffle;

import me.marcuscz.minigames.core.Core;
import me.marcuscz.minigames.api.MinigamePlayer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class EliminationPlayer extends MinigamePlayer {

    private Material item;

    public boolean checkCompleted() {
        if (completed) {
            return false;
        }
        if (player.getInventory().contains(item)) {
            player.getInventory().remove(item);
            completed = true;
            Core.getMessageManager().broadcast(EliminationMessages.PLAYER_FOUND_ITEM, player.getDisplayName());
            return true;
        }
        return false;
    }

    public void giveCompass(Location beacon) {
        Material item = Material.COMPASS;
        ItemStack itemStack = new ItemStack(item);
        ItemMeta meta = itemStack.getItemMeta();
        Objects.requireNonNull(meta).setDisplayName(Core.getMessageManager().translate(EliminationMessages.BEACON_LOCATION));
        itemStack.setItemMeta(meta);
        player.getInventory().addItem(itemStack);
        player.setCompassTarget(beacon);
    }

    public void onFail() {
        player.getInventory().clear();
        spectate = true;
        player.setGameMode(GameMode.SPECTATOR);
        Core.getMessageManager().broadcast(EliminationMessages.PLAYER_FAILED, player.getDisplayName());
    }

    public void setItem(Material item) {
        completed = false;
        this.item = item;
        Core.getMessageManager().sendMessage(EliminationMessages.YOUR_ITEM, player, item.toString());
    }

    public void peekItem(Material item) {
        Core.getMessageManager().sendMessage(EliminationMessages.NEXT_ITEM, player, item.toString());
    }
}
