package me.marcuscz.minigames.utils.phase;

import org.bukkit.Material;

import java.util.Random;

public class PhaseItem {

    private final Material item;
    private int chance;

    public PhaseItem(Material item) {
        this.item = item;
        int baseChance = 50;
        Random random = new Random();
        chance = baseChance + random.nextInt(40);
    }

    public void increaseChance(int by)
    {
        chance -= by;
    }

    public int getChance() {
        return chance;
    }

    public Material getItem() {
        return item;
    }

    @Override
    public String toString() {
        return "PhaseItem{" +
                "item=" + item +
                ", chance=" + chance +
                '}';
    }
}
