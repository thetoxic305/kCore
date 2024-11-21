package me.vifez.core.chat.commands;

import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.chat.packets.MessagePacket;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

public class GlobalcastCommand extends Command {

    public GlobalcastCommand() {
        super("globalcast");

        addAliases("gc");
        setPermission("core.chat.globalcast");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 0) {
            String message = StringUtils.join(args, " ");
            new MessagePacket(message).send();
            return;
        }

        sender.sendMessage(CC.RED + "Usage: /broadcast <message...>");
    }

}
