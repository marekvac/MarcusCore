package me.marcuscz.minigames.core;

import dev.jorel.commandapi.CommandAPICommand;

public class CoreCommands {

    private final Core core;

    public CoreCommands(Core core) {
        this.core = core;
    }

    public CommandAPICommand registerCommands(CommandAPICommand command) {
        return command.withSubcommand(new CommandAPICommand("core")
                .withSubcommand(new CommandAPICommand("setlobby")
                        .withPermission("minigame.admin")
                        .executesPlayer((player, args) -> {
                            core.getCoreOptions().lobby = player.getLocation();
                            core.getCoreOptions().save();
                            player.sendMessage("§2Lobby location set!");
                        })
                )

        );
    }

}
