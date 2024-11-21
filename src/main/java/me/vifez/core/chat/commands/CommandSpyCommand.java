package me.vifez.core.chat.commands;

import me.vifez.core.kCore;
import me.vifez.core.util.CC;
import me.vifez.core.util.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommandSpyCommand extends Command {

    private final kCore core;
    private final Map<Player, Boolean> cmdSpyStatus;

    public CommandSpyCommand(kCore core) {
        super("cmdspy");
        this.core = core;
        this.cmdSpyStatus = new HashMap<>();
        setPermission("core.cmdspy");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.RED + "This command can only be run by players.");
            return;
        }

        Player player = (Player) sender;

        boolean isCurrentlyEnabled = cmdSpyStatus.getOrDefault(player, false);
        cmdSpyStatus.put(player, !isCurrentlyEnabled);

        if (!isCurrentlyEnabled) {
            player.sendMessage(CC.GREEN + "Command spy enabled.");
        } else {
            player.sendMessage(CC.RED + "Command spy disabled.");
        }
    }

    public void onCommandExecuted(String command, Player executor) {
        if (cmdSpyStatus.getOrDefault(executor, false)) {
            String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
            String message = CC.translate(core.getMessage("cmdspy.prefix")
                    .replace("<command>", command)
                    .replace("%time%", time)
                    .replace("coloredName", executor.getDisplayName()));

            for (Player staff : core.getServer().getOnlinePlayers()) {
                if (staff.hasPermission("core.cmdspy")) {
                    staff.sendMessage(message);
                }
            }
        }
    }
}