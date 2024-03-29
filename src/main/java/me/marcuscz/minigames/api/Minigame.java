package me.marcuscz.minigames.api;

import dev.jorel.commandapi.CommandAPICommand;
import me.marcuscz.minigames.api.exceptions.InitException;
import me.marcuscz.minigames.api.exceptions.MessageException;
import me.marcuscz.minigames.api.managers.PlayerManager;
import org.bukkit.entity.Player;

import java.io.FileNotFoundException;

public abstract class Minigame<T extends MinigamePlayer, O extends MinigameOptions> {

    protected final PlayerManager<T> playerManager;
    protected O options;

    public Minigame() {
        this.playerManager = new PlayerManager<>();
    }

    public abstract void loadOptions() throws FileNotFoundException;
    public abstract void registerMessages() throws MessageException;
    public abstract void registerListeners();
    public abstract CommandAPICommand registerCommands(CommandAPICommand command);
    public abstract void init() throws InitException;
    public abstract void onStart();
    public abstract void onStop();
    public void onLatePlayerJoin(Player p) {}

    public PlayerManager<T> getPlayerManager() {
        return playerManager;
    }

    public O getOptions() {
        return options;
    }
}
