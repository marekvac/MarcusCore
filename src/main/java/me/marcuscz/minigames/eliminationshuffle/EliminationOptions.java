package me.marcuscz.minigames.eliminationshuffle;

import me.marcuscz.minigames.core.MinigameOptions;
import org.bukkit.Location;

public class EliminationOptions extends MinigameOptions {

    public EliminationOptions() {
        filename = "elimination.json";
    }

    public int time = -1;
    public boolean sameItems = true;
    public double border = 0D;
    public boolean giveFood = true;
    public boolean giveCompass = true;
    public boolean autoResume = false;
    public Location spawn = null;
    public Location beacon = null;


}
