package me.marcuscz.minigames.core.managers;

import me.marcuscz.minigames.core.MinigamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PlayerManager<T extends MinigamePlayer> {

    private final Map<UUID, T> players;

    public PlayerManager() {
        players = new HashMap<>();
    }

    public void registerPlayers(Supplier<T> ctor) {
        players.clear();
        Bukkit.getServer().getOnlinePlayers().forEach(p -> {
            if (T.canBeRegistered(p)) {
                T player = ctor.get();
                player.init(p);
                players.put(p.getUniqueId(), player);
            }
        });
    }

    public void executeAll(Consumer<T> action) {
        executeAll(action, false);
    }

    public void executeAll(Consumer<T> action, boolean activeOnly) {
        if (activeOnly) {
            players.values().stream().filter(MinigamePlayer::playing).forEach(action);
        } else {
            players.values().forEach(action);
        }
    }

    public Map<UUID, T> getPlayers() {
        return players;
    }

    public void teleportAllPlayers(Location location) {
        players.values().forEach(p -> p.getPlayer().teleport(location));
    }

    public void setGameModeAllPlayers(GameMode gameMode) {
        players.values().forEach(p -> p.getPlayer().setGameMode(gameMode));
    }

    public boolean isMinigamePlayer(UUID uuid) {
        return players.containsKey(uuid);
    }

    public T getPlayer(UUID uuid) {
        return players.get(uuid);
    }

    public int getActivePlayers() {
        return (int) players.values().stream().filter(MinigamePlayer::playing).count();
    }

    public int getCompletedPlayers() {
        return (int) players.values().stream().filter(MinigamePlayer::isCompleted).count();
    }

    public void failPlayers() {
        List<MinigamePlayer> remain = players.values().stream().filter(
                Predicate.not(MinigamePlayer::isCompleted)
                        .and(MinigamePlayer::playing)).collect(Collectors.toList());
        if (remain.size() >= 1 && this.getActivePlayers() > 1) {
            remain.forEach(MinigamePlayer::onFail);
        }
    }

    public T getWinner() {

        List<T> p = players.values().stream().filter(MinigamePlayer::playing).collect(Collectors.toList());
        if (p.size() > 0) {
            return p.get(0);
        }
        return null;
    }

    public boolean checkOnlinePlayers() {
        AtomicBoolean changed = new AtomicBoolean(false);
        players.forEach((k,p) -> {
            if (!p.getPlayer().isOnline()) {
                players.remove(k);
                changed.set(true);
            }
        });
        return changed.get();
    }
}
