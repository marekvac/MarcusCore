package me.marcuscz.minigames.eliminationshuffle;

import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class EliminationCommands {

    private final EliminationShuffle minigame;

    public EliminationCommands(EliminationShuffle minigame) {
        this.minigame = minigame;
    }

    public CommandAPICommand registerCommands(CommandAPICommand command) {
        return command.withSubcommand(new CommandAPICommand("elimination")
                .withSubcommand(new CommandAPICommand("setspawn")
                        .withPermission("minigame.admin")
                        .executesPlayer((player, args) -> {
                            minigame.setSpawn(player.getLocation());
                            player.sendMessage("§2Spawn location set");
                        })
                )
                .withSubcommand(new CommandAPICommand("setbeacon")
                        .withPermission("minigame.admin")
                        .executesPlayer((player, args) -> {
                            Block block = player.getTargetBlock(null, 4);
                            if (block.getType() != Material.BEACON) {
                                player.sendMessage("§cYou must look on a beacon");
                                return;
                            }
                            minigame.setBeacon(block.getLocation());
                            player.sendMessage("§2Beacon block set");
                        })
                )
        );
    }


}
