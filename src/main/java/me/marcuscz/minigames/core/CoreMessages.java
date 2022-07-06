package me.marcuscz.minigames.core;

public enum CoreMessages implements MinigameMessages {

    JOINED_AS_SPECTATOR;

    private final String path;

    CoreMessages() {
        this.path = "core." + this.toString().toLowerCase();
    }

    @Override
    public String getPath() {
        return path;
    }
}
