package me.marcuscz.minigames.utils.phase;

import org.bukkit.Material;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Collection;

public class ItemManager {

    private final PhaseManager phaseManager;

    public ItemManager() throws IOException, ParseException {
        phaseManager = new PhaseManager();
    }

    public void nextRound(double skipFactor) {
        phaseManager.increaseRounds(skipFactor);
    }

    public Material getRandomItem() {
        return phaseManager.getItem();
    }
}
