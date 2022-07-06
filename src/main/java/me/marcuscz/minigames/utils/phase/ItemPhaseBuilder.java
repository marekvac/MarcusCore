package me.marcuscz.minigames.utils.phase;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public class ItemPhaseBuilder {

    private final List<Material> items;

    public ItemPhaseBuilder() {
        this.items = new ArrayList<>();
    }

    public void addItem(Material item) {
        items.add(item);
    }

    public ItemPhaseBuilder addItems(Material ... items) {
        this.items.addAll(Arrays.stream(items).toList());
        return this;
    }

    public List<Material> getItems() {
        return new ArrayList<>(new LinkedHashSet<>(this.items));
    }

}
