package me.vifez.core.profile.commands;

import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NightCommand extends Command {

    public NightCommand() {
        super("night");

        setPlayerOnly(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        player.setPlayerTime(18000, false);
        sender.sendMessage(CC.YELLOW + "Set your time to night.");
    }

}