package me.marcuscz.minigames.core.managers;

import me.marcuscz.minigames.core.Core;
import me.marcuscz.minigames.core.MinigameMessages;
import me.marcuscz.minigames.core.exceptions.MessageException;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class MessageManager {

    private final Map<String, String> messages;

    public MessageManager() {
        messages = new HashMap<>();
    }

    public void register(Class<? extends MinigameMessages> messages) throws MessageException {
        MinigameMessages[] keys = messages.getEnumConstants();
        for (MinigameMessages key : keys) {
            String value = Core.getInstance().getConfig().getString("Messages." + key.getPath());
            if (value == null) {
                throw new MessageException("Message key [" + key.getPath() + "] was not found in config file");
            }
            this.messages.put(key.getPath(), value);
        }
    }

    public String translate(MinigameMessages key) {
        return translate(key.getPath());
    }

    public String translate(MinigameMessages key, String ...args) {
        return translate(key.getPath(), args);
    }

    public String translate(String key) {
        return messages.getOrDefault(key, key);
    }

    public String translate(String key, String ...args) {
        String message = translate(key);
        for (int i = 0; i < args.length; i++) {
            message = message.replace("{" + i + "}", args[i]);
        }
        return message;
    }

    public void broadcast(MinigameMessages key) {
        Core.getInstance().getServer().broadcastMessage(translate(key.getPath()));
    }

    public void broadcast(MinigameMessages key, String ...args) {
        String message = translate(key.getPath(), args);
        Core.getInstance().getServer().broadcastMessage(message);
    }

    public void sendMessage(MinigameMessages key, Player player) {
        player.sendMessage(translate(key.getPath()));
    }

    public void sendMessage(MinigameMessages key, Player player, String ...args) {
        player.sendMessage(translate(key.getPath(), args));
    }

}
