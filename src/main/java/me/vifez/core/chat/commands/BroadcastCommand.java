package me.vifez.core.chat.commands;

import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class BroadcastCommand extends Command {

    public BroadcastCommand() {
        super("broadcast");

        addAliases("bc");
        setPermission("core.chat.broadcast");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 0) {
            String message = StringUtils.join(args, " ");
            Bukkit.broadcastMessage(CC.translate(message));
            return;
        }

        sender.sendMessage(CC.RED + "Usage: /broadcast <message...>");
    }

}
