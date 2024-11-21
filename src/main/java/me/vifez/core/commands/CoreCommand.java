package me.vifez.core.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.CommandPermission;
import me.vifez.core.kCore;
import me.vifez.core.util.CC;
import org.bukkit.command.CommandSender;

@CommandAlias("core|kCore")
public class CoreCommand extends BaseCommand {

    private final kCore plugin;

    public CoreCommand(kCore plugin) {
        this.plugin = plugin;
    }

    @Default
    public void onCoreCommand(CommandSender sender) {
        sender.sendMessage(CC.translate("&7&m------------------------------"));
        sender.sendMessage(CC.translate("&bkCore core"));
        sender.sendMessage(CC.translate("&7&m------------------------------"));
        sender.sendMessage(CC.translate("&fVersion: &b1.4"));
        sender.sendMessage(CC.translate("&fAuthor: &bKrypton Development"));
        sender.sendMessage(CC.translate("&7&m------------------------------"));
    }

    @Subcommand("reload")
    @CommandPermission("core.reload")
    public void onReloadCommand(CommandSender sender) {
        reloadPlugin();
        sender.sendMessage(CC.translate("&eSuccessfully reloaded &bCore's &econfigurations."));
    }

    private void reloadPlugin() {
        plugin.reloadConfig();

        plugin.loadMessages();
    }
}