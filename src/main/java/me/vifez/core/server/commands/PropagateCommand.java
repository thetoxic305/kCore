package me.vifez.core.server.commands;

import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.kCore;
import me.vifez.core.server.Server;
import me.vifez.core.server.packets.ServerRunCommandPacket;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PropagateCommand extends Command {

    private final kCore core;

    public PropagateCommand(kCore core) {
        super("propagate");

        this.core = core;

        setPermission("core.server.propogate");
        addAliases("prop", "runcommand", "runcmd");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length > 1) {
            Server server = core.getServerHandler().getServer(args[0]);

            if (server == null && !args[0].equalsIgnoreCase("all")) {
                sender.sendMessage(CC.RED + "No server with name " + args[0] + " not found.");
                return;
            }
            String serverName = server == null ? "all servers" : server.getName();
            String command = StringUtils.join(args, " ", 1, args.length);
            new ServerRunCommandPacket(serverName, command).send();
            sender.sendMessage(CC.YELLOW + "Running command " + CC.WHITE + command + CC.YELLOW + " on " + CC.GOLD + serverName + CC.YELLOW + '.');
            return;
        }

        sender.sendMessage(CC.RED + "Usage: /propagate <server> <command...>");
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> toReturn = new ArrayList<>();
            for (Server server : core.getServerHandler().getServers()) {
                toReturn.add(server.getName());
            }
            toReturn.add("all");
            return toReturn;
        }

        return Collections.emptyList();
    }

}