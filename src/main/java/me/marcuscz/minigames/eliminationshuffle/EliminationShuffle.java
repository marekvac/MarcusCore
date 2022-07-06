package me.marcuscz.minigames.eliminationshuffle;

import dev.jorel.commandapi.CommandAPICommand;
import me.marcuscz.minigames.api.Constants;
import me.marcuscz.minigames.core.Core;
import me.marcuscz.minigames.api.Minigame;
import me.marcuscz.minigames.api.MinigameOptions;
import me.marcuscz.minigames.api.exceptions.InitException;
import me.marcuscz.minigames.api.exceptions.MessageException;
import me.marcuscz.minigames.eliminationshuffle.listeners.PlayerInteractEventListener;
import me.marcuscz.minigames.eliminationshuffle.listeners.PlayerQuitEventListener;
import me.marcuscz.minigames.utils.phase.ItemManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class EliminationShuffle extends Minigame<EliminationPlayer, EliminationOptions> {

    private int timer;
    private boolean active;
    private boolean paused;
    private int currentRound;
    private ItemManager itemManager;
    private final EliminationCommands commands;
    public static final String name = "elimination";
    private Queue<Material> items;
    private BossBar bossBar;

    public EliminationShuffle() {
        super();
        commands = new EliminationCommands(this);
    }

    @Override
    public void loadOptions() throws FileNotFoundException {
        options = MinigameOptions.load(EliminationOptions::new);
    }

    @Override
    public void registerMessages() throws MessageException {
        Core.getMessageManager().register(EliminationMessages.class);
    }

    @Override
    public void registerListeners() {
        Core.registerListener(new PlayerInteractEventListener(this));
        Core.registerListener(new PlayerQuitEventListener(this));
    }

    @Override
    public CommandAPICommand registerCommands(CommandAPICommand command) {
        return commands.registerCommands(command);
    }

    @Override
    public void init() throws InitException {
        try {
            itemManager = new ItemManager();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            throw new InitException("failed to initialize phase manager");
        }
        if (options.spawn == null || options.spawn.getWorld() == null) {
            throw new InitException("Invalid spawn block");
        }
        World world = options.spawn.getWorld();
        world.setSpawnLocation(options.spawn);
        world.getWorldBorder().setCenter(options.spawn);
        double size = options.border > 0 ? options.border : Constants.maxBorderSize;
        world.getWorldBorder().setSize(size);
        currentRound = 0;
        active = paused = false;
        playerManager.registerPlayers(EliminationPlayer::new);

        items = new LinkedList<>();
        int pSize = playerManager.getActivePlayers();
        for (int i = 1; i < pSize; i++) {
            items.add(itemManager.getRandomItem());
            itemManager.nextRound(1);
        }

        bossBar = Core.getInstance().getServer().createBossBar("Current item: null", BarColor.WHITE, BarStyle.SOLID);
        bossBar.setVisible(false);
        Core.getInstance().getServer().getOnlinePlayers().forEach(player -> bossBar.addPlayer(player));
    }

    @Override
    public void onStart() {
        playerManager.setGameModeAllPlayers(GameMode.SURVIVAL);
        playerManager.executeAll(p -> p.getPlayer().getInventory().clear());
        playerManager.executeAll(p -> p.getPlayer().setFoodLevel(20));
        playerManager.executeAll(p -> p.getPlayer().setHealth(p.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()));
        if (options.giveFood) playerManager.executeAll(EliminationPlayer::giveFood);
        if (options.giveCompass) playerManager.executeAll(p -> p.giveCompass(options.beacon));
        playerManager.teleportAllPlayers(options.spawn);
        paused = true;
        active = true;
        nextRound();
        Core.getMessageManager().broadcast(EliminationMessages.GAME_STARTED);
    }

    @Override
    public void onStop() {
//        playerManager.teleportAllPlayers(Core.getInstance().lobby);
        active = false;
        Core.getMessageManager().broadcast(EliminationMessages.GAME_STOPPED);
        bossBar.removeAll();
    }

    @Override
    public void onPause() {
        paused = true;
    }

    @Override
    public void onResume() {
        if (timer == 0) {
            nextRound();
            return;
        }
        paused = false;
    }

    @Override
    public void onSkip() {
        endRound();
    }

    @Override
    public void onLatePlayerJoin(Player p) {
        bossBar.addPlayer(p);
    }

    public boolean endRound() {
        onPause();
        bossBar.setVisible(false);

        // remove failed players
        playerManager.failPlayers();
        if (playerManager.getActivePlayers() > 1) {
            nextRound();
            return true;
        } else {
            if (playerManager.getWinner() != null) {
                Core.getMessageManager().broadcast(EliminationMessages.PLAYER_WON, playerManager.getWinner().getPlayer().getDisplayName());
            } else {
                Core.getLog().severe("Error, winner is null");
                Core.getMessageManager().broadcast(EliminationMessages.NO_WINNER);
            }
            return false;
        }
    }

    public void nextRound() {
        if (playerManager.checkOnlinePlayers() && checkIfAllCompleted()) {
            return;
        }

        currentRound++;
        itemManager.nextRound(1);
        timer = options.time * 20;

        Material item = items.remove();
        playerManager.executeAll(p -> p.setItem(item), true);
        bossBar.setTitle(Core.getMessageManager().translate(EliminationMessages.CURRENT_ITEM, item.toString()));
        bossBar.setVisible(true);

        paused = false;
        Core.getMessageManager().broadcast(EliminationMessages.ROUND, Integer.toString(currentRound));
    }

    public boolean checkIfAllCompleted() {
        if (playerManager.getActivePlayers() - 1 <= playerManager.getCompletedPlayers()) {
            Core.getMessageManager().broadcast(EliminationMessages.EVERYONE_COMPLETED);
            endRound();
            return true;
        }
        return false;
    }

    @Override
    public void onTick() {

        if (timer < 0) return; // timer disabled

        if (!active || paused) return;

        if (timer <= 0) {
            Core.getInstance().getServer().getScheduler().callSyncMethod(Core.getInstance(), this::endRound);
            return;
        }

        if (timer <= 200) {
            if (timer % 20 == 0) {
                int n = (timer / 20);
                Core.getMessageManager().broadcast(EliminationMessages.TIME_REMAIN, Integer.toString(n));
            }
        }

        timer--;
    }

    public void setSpawn(Location spawn) {
        options.spawn = spawn;
        options.save();
    }

    public void setBeacon(Location beacon) {
        options.beacon = beacon;
        options.save();
    }

    public Location getBeacon() {
        return options.beacon;
    }

    public Queue<Material> getItems() {
        return items;
    }

    public BossBar getBossBar() {
        return bossBar;
    }
}
