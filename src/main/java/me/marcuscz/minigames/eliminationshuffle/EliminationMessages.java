package me.marcuscz.minigames.eliminationshuffle;

import me.marcuscz.minigames.api.MinigameMessages;

public enum EliminationMessages implements MinigameMessages {
    GAME_STARTED,
    YOUR_ITEM,
    EVERYONE_COMPLETED,
    TIME_REMAIN,
    PLAYER_FAILED,
    PLAYER_WON,
    ROUND,
    PLAYER_FOUND_ITEM,
    NEXT_ITEM,
    GAME_STOPPED,
    NO_WINNER,
    BEACON_LOCATION,
    CURRENT_ITEM;

    private final String path;

    EliminationMessages() {
        path = EliminationShuffle.name + "." + this.toString().toLowerCase();
    }

    @Override
    public String getPath() {
        return path;
    }
}
