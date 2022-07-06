package me.marcuscz.minigames.api;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public abstract class MinigamePlayer {

    protected Player player;
    protected UUID uuid;
    protected int score;
    protected boolean spectate;
    protected boolean completed;

    public void init(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
    }

    public abstract boolean checkCompleted();
    public abstract void onFail();

    public static boolean canBeRegistered(Player p) {
        return p.getGameMode() != GameMode.CREATIVE;
    }

    public void giveFood() {
        ItemStack stack = new ItemStack(Material.COOKED_BEEF);
        stack.setAmount(32);
        player.getInventory().addItem(stack);
    }

    public void setSpawnPoint(Location spawn) {
        player.setBedSpawnLocation(spawn, true);
    }

    public Player getPlayer() {
        return player;
    }

    public boolean playing() {
        return !spectate;
    }

    public boolean isCompleted() {
        return completed;
    }
}
