package me.vifez.core.chat;

import me.vifez.core.kCore;
import me.vifez.core.chat.commands.CommandSpyCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandSpyListener implements Listener {

    private final CommandSpyCommand commandSpyCommand;

    public CommandSpyListener(kCore core) {
        this.commandSpyCommand = core.getCommandSpyCommand();
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        commandSpyCommand.onCommandExecuted(event.getMessage(), event.getPlayer());
    }
}