package me.marcuscz.minigames.api.managers;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import me.marcuscz.minigames.core.Core;
import me.marcuscz.minigames.api.exceptions.InitException;
import me.marcuscz.minigames.api.exceptions.StartException;
import me.marcuscz.minigames.api.exceptions.UnknownMinigameException;

public class CommandManager {

    public CommandAPICommand command;

    public CommandManager() {
        command = new CommandAPICommand("game")
                .withPermission("minigame.admin")
                .withSubcommand(new CommandAPICommand("list")
                        .executes((commandSender, objects) -> {
                            commandSender.sendMessage("Available minigames:");
                            Core.getMinigameManager().getMinigameList().forEach(m -> commandSender.sendMessage("§b" + m));
                    })
                )
                .withSubcommand(new CommandAPICommand("init")
                        .withArguments(new StringArgument("game"))
                        .executes((commandSender, args) -> {
                            MinigameManager manager = Core.getMinigameManager();
                            String name = (String) args[0];
                            if (!manager.getMinigameList().contains(name)) {
                                commandSender.sendMessage("§cInvalid minigame");
                                return;
                            }
                            try {
                                manager.initMinigame(name);
                                commandSender.sendMessage("§2Minigame initialized. Ready to start!");
                            } catch (InitException e) {
                                commandSender.sendMessage("§cFailed init minigame: §4" + e.getMessage());
                            }
                        })
                )
                .withSubcommand(new CommandAPICommand("start")
                        .executes((commandSender, args) -> {
                            try {
                                Core.getMinigameManager().start();
                            } catch (UnknownMinigameException | StartException e) {
                                commandSender.sendMessage("§4" + e.getMessage());
                            }
                        })
                )
                .withSubcommand(new CommandAPICommand("stop")
                        .executes((commandSender, args) -> {
                            try {
                                Core.getMinigameManager().stop();
                            } catch (UnknownMinigameException e) {
                                commandSender.sendMessage("§4" + e.getMessage());
                            }
                        })
                )
                .withSubcommand(new CommandAPICommand("pause")
                        .executes((commandSender, args) -> {
                            try {
                                Core.getMinigameManager().pause();
                            } catch (UnknownMinigameException e) {
                                commandSender.sendMessage("§4" + e.getMessage());
                            }
                        })
                )
                .withSubcommand(new CommandAPICommand("resume")
                        .executes((commandSender, args) -> {
                            try {
                                Core.getMinigameManager().resume();
                            } catch (UnknownMinigameException e) {
                                commandSender.sendMessage("§4" + e.getMessage());
                            }
                        })
                )
                .withSubcommand(new CommandAPICommand("skip")
                        .executes((commandSender, args) -> {
                            try {
                                Core.getMinigameManager().skip();
                            } catch (UnknownMinigameException e) {
                                commandSender.sendMessage("§4" + e.getMessage());
                            }
                        })
                )
        ;
    }

    public void register() {
        command.register();
    }
}
