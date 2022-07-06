package me.marcuscz.minigames.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.marcuscz.minigames.api.MinigameOptions;
import me.marcuscz.minigames.api.exceptions.MessageException;
import me.marcuscz.minigames.api.exceptions.UnknownMinigameException;
import me.marcuscz.minigames.core.listeners.PlayerJoinListener;
import me.marcuscz.minigames.api.managers.CommandManager;
import me.marcuscz.minigames.api.managers.MessageManager;
import me.marcuscz.minigames.api.managers.MinigameManager;
import me.marcuscz.minigames.eliminationshuffle.EliminationShuffle;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

public final class Core extends JavaPlugin {

    private static Core _instance;
    private static MessageManager messageManager;
    private static MinigameManager minigameManager;
    private static CommandManager commandManager;
    public static Gson gson;
    private CoreOptions coreOptions;

    @Override
    public void onEnable() {
        _instance = this;
        saveDefaultConfig();
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Location.class, new MinigameOptions.LocationAdapter());
        gson = builder.create();

        try {
            coreOptions = CoreOptions.load(CoreOptions::new);
        } catch (FileNotFoundException e) {
            getLogger().severe("Failed to load Core options file!");
            e.printStackTrace();
            onDisable();
            return;
        }

        if (coreOptions.lobby == null) {
            getLogger().warning("Lobby location is not set yet! Players shouldn't join the server and minigames cannot be launched without the lobby location. Please set the lobby location using /game core setlobby");
        }

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

        messageManager = new MessageManager();
        minigameManager = new MinigameManager();
        commandManager = new CommandManager();

        CoreCommands commands = new CoreCommands(this);
        commandManager.command = commands.registerCommands(commandManager.command);

        try {
            messageManager.register(CoreMessages.class);
        } catch (MessageException e) {
            Core.getLog().severe("Failed register messages for CORE");
            Core.getLog().severe(e.getMessage());
            onDisable();
            return;
        }

        minigameManager.register(EliminationShuffle.name, new EliminationShuffle());

        minigameManager.loadMinigames();
    }

    @Override
    public void onDisable() {
        try {
            minigameManager.stop();
        } catch (UnknownMinigameException ignored) {

        }
        HandlerList.unregisterAll(this);
    }

    public static Core getInstance() {
        return _instance;
    }

    public static void registerListener(Listener listener) {
        getInstance().getServer().getPluginManager().registerEvents(listener, getInstance());
    }

    public static MessageManager getMessageManager() {
        return messageManager;
    }

    public static MinigameManager getMinigameManager() {
        return minigameManager;
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }

    public static Logger getLog() {
        return getInstance().getLogger();
    }

    public CoreOptions getCoreOptions() {
        return coreOptions;
    }
}
