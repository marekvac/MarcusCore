package me.marcuscz.minigames.api;

import org.bukkit.event.Listener;

public abstract class MinigameEventListener<T extends Minigame<?, ?>> implements Listener {

    protected final T minigame;

    public MinigameEventListener(T minigame) {
        this.minigame = minigame;
    }
}
