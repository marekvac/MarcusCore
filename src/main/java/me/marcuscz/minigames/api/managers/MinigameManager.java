package me.marcuscz.minigames.api.managers;

import me.marcuscz.minigames.api.minigame.PausableMinigame;
import me.marcuscz.minigames.api.minigame.SkippableMinigame;
import me.marcuscz.minigames.api.minigame.TickingMinigame;
import me.marcuscz.minigames.core.Core;
import me.marcuscz.minigames.api.Minigame;
import me.marcuscz.minigames.api.exceptions.InitException;
import me.marcuscz.minigames.api.exceptions.MessageException;
import me.marcuscz.minigames.api.exceptions.StartException;
import me.marcuscz.minigames.api.exceptions.UnknownMinigameException;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MinigameManager {

    private final Map<String, Minigame<?, ?>> minigames;
    private String selectedMinigame = null;
    private boolean minigameActive;

    public MinigameManager() {
        this.minigames = new HashMap<>();
    }

    public void register(String name, Minigame<?, ?> minigame) {
        this.minigames.put(name, minigame);
    }

    public void loadMinigames() {
        CommandManager commandManager = Core.getCommandManager();
        minigames.forEach((name, minigame) -> {
            Core.getLog().info("Loading minigame: " + name + "...");

            try {
                minigame.loadOptions();
            } catch (FileNotFoundException e) {
                Core.getLog().severe("Failed load options for minigame " + name);
                Core.getLog().severe(e.getMessage());
                return;
            }

            try {
                minigame.registerMessages();
            } catch (MessageException e) {
                Core.getLog().severe("Failed register messages for minigame " + name);
                Core.getLog().severe(e.getMessage());
                return;
            }

            minigame.registerListeners();
            commandManager.command = minigame.registerCommands(commandManager.command);

            Core.getLog().info("Successfully loaded minigame " + name);
        });
        commandManager.register();
    }

    public Set<String> getMinigameList() {
        return minigames.keySet();
    }

    public void initMinigame(String name) throws InitException {
        if (minigameActive) {
            throw new InitException("Some minigame is already active!");
        }
        Minigame<?,?> minigame = minigames.get(name);
        minigame.init();

        if (minigame instanceof TickingMinigame) {
            Core.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(Core.getInstance(), ((TickingMinigame) minigame)::onTick, 1, 1);
        }
        selectedMinigame = name;
    }

    public void start() throws UnknownMinigameException, StartException {
        if (Core.getInstance().getCoreOptions().lobby == null) {
            throw new StartException("Global lobby location is not set! Please set the lobby location using /game core setlobby, and restart your server.");
        }
        if (minigameActive) {
            throw new StartException("Some minigame is already active");
        }
        if (selectedMinigame == null) throw new UnknownMinigameException("No minigame selected");
        minigames.get(selectedMinigame).onStart();
        minigameActive = true;
    }

    public void stop() throws UnknownMinigameException {
        if (selectedMinigame == null) throw new UnknownMinigameException("No minigame selected");
        minigames.get(selectedMinigame).onStop();
        selectedMinigame = null;
        minigameActive = false;
        Core.getInstance().getServer().getScheduler().cancelTasks(Core.getInstance());
    }

    public void pause() throws UnknownMinigameException {
        if (selectedMinigame == null) throw new UnknownMinigameException("No minigame selected");

        Minigame<?,?> minigame = minigames.get(selectedMinigame);
        if (minigame instanceof PausableMinigame) {
            ((PausableMinigame) minigame).onPause();
        } else {
            throw new UnknownMinigameException("This minigame doesn't support pausing");
        }
    }

    public void resume() throws UnknownMinigameException {
        if (selectedMinigame == null) throw new UnknownMinigameException("No minigame selected");

        Minigame<?,?> minigame = minigames.get(selectedMinigame);
        if (minigame instanceof PausableMinigame) {
            ((PausableMinigame) minigame).onResume();
        } else {
            throw new UnknownMinigameException("This minigame doesn't support pausing");
        }
    }

    public void skip() throws UnknownMinigameException {
        if (selectedMinigame == null) throw new UnknownMinigameException("No minigame selected");

        Minigame<?,?> minigame = minigames.get(selectedMinigame);
        if (minigame instanceof SkippableMinigame) {
            ((SkippableMinigame) minigame).onSkip();
        } else {
            throw new UnknownMinigameException("This minigame doesn't support skipping");
        }
    }

    public boolean isMinigameActive() {
        return minigameActive && selectedMinigame != null;
    }

    public Minigame<?,?> getActiveMinigame() {
        if (selectedMinigame == null) {
            return null;
        }
        return minigames.get(selectedMinigame);
    }
}
