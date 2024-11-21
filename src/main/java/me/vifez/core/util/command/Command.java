package me.vifez.core.util.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Command {

    private final String label;

    private String permission;
    private boolean playerOnly;
    private List<String> aliases = new ArrayList<>();

    public Command(String label) {
        this.label = label;
    }

    public abstract void execute(CommandSender sender, String[] args);

    public List<String> complete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    public List<String> getPlayerNames() {
        List<String> toReturn = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            toReturn.add(player.getName());
        }

        return toReturn;
    }

    public String getLabel() {
        return label;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean isPlayerOnly() {
        return playerOnly;
    }

    public void setPlayerOnly(boolean playerOnly) {
        this.playerOnly = playerOnly;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void addAliases(List<String> aliases) {
        this.aliases.addAll(aliases);
    }

    public void addAliases(String... aliases) {
        this.aliases.addAll(Arrays.asList(aliases));
    }

}