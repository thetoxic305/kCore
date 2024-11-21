package me.vifez.core.profile.commands;

import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DayCommand extends Command {

    public DayCommand() {
        super("day");

        setPlayerOnly(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        player.setPlayerTime(1000, false);
        sender.sendMessage(CC.YELLOW + "Set your time to day.");
    }

}