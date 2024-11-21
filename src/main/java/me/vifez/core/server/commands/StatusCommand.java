package me.vifez.core.server.commands;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.kCore;
import me.vifez.core.server.Server;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatusCommand extends Command {

    private final kCore core;

    public StatusCommand(kCore core) {
        super("status");

        this.core = core;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0 || args.length == 1) {
            Server server;

            if (args.length == 0) {
                server = core.getServerHandler().getLocalServer();
            } else {
                server = core.getServerHandler().getServer(args[0]);

                if (server == null) {
                    sender.sendMessage(CC.translate(core.getMessages().getString("status.server-not-found")
                            .replace("{server}", args[0])));
                    return;
                }
            }

            int online = server.getOnlinePlayers();
            if (!sender.hasPermission("core.status")) {
                online -= server.getVanishedPlayers();
            }

            String[] tpsAvg = new String[server.getTPS().length];
            for (int i = 0; i < server.getTPS().length; i++) {
                tpsAvg[i] = format(server.getTPS()[i]);
            }

            sender.sendMessage(CC.translate(new String[]{
                    kCoreConstant.CHAT_LINE,
                    core.getMessages().getString("status.header").replace("{server}", server.getName()),
                    core.getMessages().getString("status.status").replace("{status}", server.getStatus().getText()),
                    core.getMessages().getString("status.online-players")
                            .replace("{online}", String.valueOf(online))
                            .replace("{max}", String.valueOf(server.getMaxPlayers())),
                    core.getMessages().getString("status.tps")
                            .replace("{tps}", StringUtils.join(tpsAvg, "&f, ")),
                    kCoreConstant.CHAT_LINE
            }));
            return;
        }

        sender.sendMessage(CC.RED + "Usage: /status [server]");
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> toReturn = new ArrayList<>();
            for (Server server : core.getServerHandler().getServers()) {
                toReturn.add(server.getName());
            }
            return toReturn;
        }

        return Collections.emptyList();
    }

    private static String format(double tps) {
        if (tps > 19.99) {
            tps = 20.0;
        }

        return ((tps > 19.5) ? CC.GREEN : CC.RED) + ((tps > 20.0) ? "*" : "") + Math.min(Math.round(tps * 100.0) / 100.0, 20.0);
    }
}