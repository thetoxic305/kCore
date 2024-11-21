package me.vifez.core.server.commands;

import me.vifez.core.kCoreConstant;
import me.vifez.core.util.command.Command;
import me.vifez.core.util.CC;
import me.vifez.core.util.PlayerUtil;
import me.vifez.core.kCore;
import me.vifez.core.server.Server;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;

public class GlobalListCommand extends Command {

    private final kCore core;

    public GlobalListCommand(kCore core) {
        super("globallist");

        this.core = core;

        addAliases("glist");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(new String[]{
                kCoreConstant.CHAT_LINE,
                CC.GOLD + "Global List"
        });
        for (Server server : core.getServerHandler().getServers()) {
            int online = server.getOnlinePlayers();
            if (!sender.hasPermission("core.server.globallist")) {
                online -= server.getVanishedPlayers();
            }

            TextComponent component = new TextComponent(kCoreConstant.INDENT + CC.YELLOW + server.getName() + CC.GRAY + ": " + CC.WHITE + online + '/' + server.getMaxPlayers());
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click" + CC.YELLOW + " to view more information about " + CC.GOLD + server.getName() + CC.YELLOW + '.').create()));

            if (PlayerUtil.isPlayer(sender)) {
                PlayerUtil.getPlayer(sender).spigot().sendMessage(component);
            } else {
                sender.sendMessage(component.getText());
            }
        }
        sender.sendMessage(kCoreConstant.CHAT_LINE);
    }

}