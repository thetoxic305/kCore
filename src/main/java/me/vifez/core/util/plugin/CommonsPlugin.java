package me.vifez.core.util.plugin;

import me.vifez.core.util.command.Command;
import me.vifez.core.util.command.CommandHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class CommonsPlugin extends JavaPlugin {

    public void registerListeners(Plugin plugin, Listener... listeners) {
        for (Listener listener : listeners) {
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    public void registerCommands(CommandHandler handler, Command... commands) {
        for (Command command : commands) {
            handler.register(command);
        }
    }

}