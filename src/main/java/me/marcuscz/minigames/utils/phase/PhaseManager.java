package me.marcuscz.minigames.utils.phase;

import me.marcuscz.minigames.core.Core;
import org.bukkit.Material;
import org.bukkit.util.FileUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class PhaseManager {

    private double roundsLasted;
    private int currentPhase;
    private final List<ItemPhase> phases;
    private final List<PhaseItem> availableItems;

    public PhaseManager() throws IOException, ParseException {
        phases = new ArrayList<>();
        availableItems = new ArrayList<>();

        File f = new File(Core.getInstance().getDataFolder().getPath() + File.separator + "phases.json");

        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdir();
        }

        if (!f.exists()) {
            InputStream is = Core.getInstance().getResource("phases.json");
            Files.copy(is, f.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader(f);
        Object obj = jsonParser.parse(reader);
        JSONArray phasesArray = (JSONArray) obj;
        phasesArray.forEach(ph -> this.parsePhase((JSONObject) ph));


        currentPhase = 0;
        availableItems.addAll(phases.get(currentPhase).getItems());

        roundsLasted = 0;
    }

    private void parsePhase(JSONObject phase) throws ClassCastException {
        Long rounds = (Long) phase.get("rounds");
        JSONArray items = (JSONArray) phase.get("items");
        ItemPhaseBuilder builder = new ItemPhaseBuilder();
        items.forEach(i -> {
            Material item = Material.matchMaterial((String) i);
            builder.addItem(item);
        });
        ItemPhase itemPhase = new ItemPhase(rounds.intValue());
        itemPhase.setItems(builder.getItems());
        this.phases.add(itemPhase);
    }

    public Material getItem() {
        return ItemPhase.getItem(availableItems);
    }

    public void increaseRounds(double by) {
        roundsLasted += by;
        if (roundsLasted > phases.get(currentPhase).getRoundDuration() && currentPhase < phases.size()-1) {
            currentPhase++;
            if (currentPhase < phases.size()) {
                ItemPhase previous = phases.get(currentPhase-1);
                previous.increaseAllItems(40);
                availableItems.addAll(phases.get(currentPhase).getItems());
                roundsLasted = 1;
                System.out.println("new phase");
            }
        }
    }
}
