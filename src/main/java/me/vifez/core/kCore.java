package me.vifez.core;

import co.aikar.commands.PaperCommandManager;
import com.mongodb.ServerAddress;
import me.vifez.core.chat.ChatHandler;
import me.vifez.core.chat.CommandSpyListener;
import me.vifez.core.chat.commands.*;
import me.vifez.core.commands.CoreCommand;
import me.vifez.core.commands.gamemode.AdventureCommand;
import me.vifez.core.commands.gamemode.CreativeCommand;
import me.vifez.core.commands.gamemode.SpectatorCommand;
import me.vifez.core.commands.gamemode.SurvivalCommand;
import me.vifez.core.grant.handler.GrantHandler;
import me.vifez.core.grant.listener.GrantListener;
import me.vifez.core.grant.task.GrantTask;
import me.vifez.core.grant.commands.ClearGrantsCommand;
import me.vifez.core.grant.commands.GrantCommand;
import me.vifez.core.grant.commands.GrantsCommand;
import me.vifez.core.profile.handler.ProfileHandler;
import me.vifez.core.profile.listener.ProfileListener;
import me.vifez.core.profile.commands.*;
import me.vifez.core.punishment.handler.PunishmentHandler;
import me.vifez.core.punishment.listener.PunishmentListener;
import me.vifez.core.punishment.commands.*;
import me.vifez.core.rank.command.RankCommand;
import me.vifez.core.rank.handler.RankHandler;
import me.vifez.core.rank.listener.RankListener;
import me.vifez.core.server.handler.ServerHandler;
import me.vifez.core.server.listener.ServerListener;
import me.vifez.core.server.task.ServerTask;
import me.vifez.core.server.commands.*;
import me.vifez.core.server.warp.commands.DeleteWarpCommand;
import me.vifez.core.server.warp.commands.SetWarpCommand;
import me.vifez.core.server.warp.commands.WarpCommand;
import me.vifez.core.staff.handler.StaffHandler;
import me.vifez.core.staff.listener.StaffListener;
import me.vifez.core.staff.StaffMode;
import me.vifez.core.staff.commands.*;
import me.vifez.core.util.CC;
import me.vifez.core.util.command.Command;
import me.vifez.core.util.command.CommandHandler;
import me.vifez.core.util.menu.MenuListener;
import me.vifez.core.util.mongo.MongoHandler;
import me.vifez.core.util.redis.RedisHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.UUID;

public class kCore extends JavaPlugin {

    private CommandSpyCommand commandSpyCommand;

    public static final UUID CLIENT_ID = UUID.randomUUID();

    private static kCore instance;

    private FileConfiguration messages;

    private RankHandler rankHandler;
    private StaffHandler staffHandler;
    private PunishmentHandler punishmentHandler;
    private GrantHandler grantHandler;
    private ProfileHandler profileHandler;
    private ServerHandler serverHandler;
    private ChatHandler chatHandler;
    private CommandHandler commandHandler;

    private long timeTracker;

    @Override
    public void onEnable() {
        instance = this;
        timeTracker = System.currentTimeMillis();

        commandSpyCommand = new CommandSpyCommand(this);

        getServer().getPluginManager().registerEvents(new CommandSpyListener(this), this);

        PaperCommandManager manager = new PaperCommandManager(this);

        saveDefaultConfig();
        loadMessages();

        MongoHandler.init(
                new ServerAddress(getConfig().getString("mongo.host"), getConfig().getInt("mongo.port")),
                getConfig().getBoolean("mongo.authentication.enabled"),
                getConfig().getString("mongo.authentication.database"),
                getConfig().getString("mongo.authentication.username"),
                getConfig().getString("mongo.authentication.password")
        );

        RedisHandler.init(
                getConfig().getString("redis.host"),
                getConfig().getInt("redis.port"),
                getConfig().getBoolean("redis.authentication.enabled"),
                getConfig().getString("redis.authentication.password")
        );

        getServer().getPluginManager().registerEvents(new MenuListener(), this);

        rankHandler = new RankHandler(this);
        staffHandler = new StaffHandler(this);
        punishmentHandler = new PunishmentHandler(this);
        grantHandler = new GrantHandler(this);
        profileHandler = new ProfileHandler(this);
        serverHandler = new ServerHandler(this);
        chatHandler = new ChatHandler(this);
        commandHandler = new CommandHandler(this);

        StaffMode.init();
        sendStartupMessage();

        manager.registerCommand(new CoreCommand(this));

        registerListeners(
                new GrantListener(this),
                new ProfileListener(this),
                new PunishmentListener(this),
                new RankListener(this),
                new StaffListener(this),
                new ServerListener()
        );

        registerCommands(
                new SpawnCommand(this),
                new SetSpawnCommand(this),

                new WarpCommand(this),
                new SetWarpCommand(this),
                new DeleteWarpCommand(this),

                new TeleportCommand(this),
                new TeleportHereCommand(this),

                new ListCommand(this),
                new GlobalListCommand(this),
                new StatusCommand(this),
                new PropagateCommand(this),

                new FlyCommand(this),
                new VanishCommand(this),
                new StaffChatCommand(this),
                new StaffModeCommand(this),
                new FreezeCommand(this),
                new InventorySeeCommand(this),

                new GrantCommand(this),
                new GrantsCommand(this),
                new ClearGrantsCommand(this),
                new PermissionsCommand(this),

                new BlacklistCommand(this),
                new BanCommand(this),
                new MuteCommand(this),
                new WarnCommand(this),
                new KickCommand(this),
                new UnblacklistCommand(this),
                new UnbanCommand(this),
                new UnmuteCommand(this),
                new ClearPunishmentsCommand(this),
                new CheckCommand(this),
                new AltsCommand(this),

                new RankCommand(this),

                new BroadcastCommand(),
                new GlobalcastCommand(),
                new ClearChatCommand(this),
                new SlowChatCommand(this),
                new MuteChatCommand(this),

                new MessageCommand(this),
                new ReplyCommand(this),
                new IgnoreCommand(this),
                new ToggleChatCommand(this),
                new TogglePrivateMessagesCommand(this),

                new DayCommand(),
                new NightCommand(),
                new PingCommand(this),

                new ReportCommand(this),
                new RequestCommand(this),

                new CommandSpyCommand(this),

                new CreativeCommand(this),
                new SurvivalCommand(this),
                new AdventureCommand(this),
                new SpectatorCommand(this)
        );

        new GrantTask(this).runTaskTimerAsynchronously(this, 100L, 100L);
        new ServerTask(this).runTaskTimerAsynchronously(this, 20L, 20L);

        new kCoreAPI(this);
    }

    public CommandSpyCommand getCommandSpyCommand() {
        return commandSpyCommand;
    }

    @Override
    public void onDisable() {
        MongoHandler.close();
        RedisHandler.close();
    }

    public void registerListeners(Listener... listeners) {
        for (Listener listener : listeners)
            getServer().getPluginManager().registerEvents(listener, this);
    }

    public void registerCommands(Command... commands) {
        for (Command command : commands)
            commandHandler.register(command);
    }

    private void sendStartupMessage() {
        logMessage("&7&m-----------------------------------------------------");
        logMessage("&ckCore core");
        logMessage(" ");
        logMessage("&fVersion: &c" + getDescription().getVersion());
        logMessage("&fProtocol: &c" + getServer().getBukkitVersion());
        logMessage("&fAuthors: &c" + String.join(", ", getDescription().getAuthors()));
        logMessage(" ");
        logMessage("&fSpigot: &c" + getServer().getName());
        logMessage("&fLoaded in &c" + (System.currentTimeMillis() - timeTracker) + "ms");
        logMessage("&7&m-----------------------------------------------------");
    }

    private void logMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(CC.translate(message));
    }

    public void loadMessages() {
        File messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            saveResource("messages.yml", false);
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public FileConfiguration getMessages() {
        return messages;
    }

    public void reloadMessages() {
        loadMessages();
    }

    public static kCore getInstance() {
        return instance;
    }

    public RankHandler getRankHandler() {
        return rankHandler;
    }

    public StaffHandler getStaffHandler() {
        return staffHandler;
    }

    public PunishmentHandler getPunishmentHandler() {
        return punishmentHandler;
    }

    public GrantHandler getGrantHandler() {
        return grantHandler;
    }

    public ProfileHandler getProfileHandler() {
        return profileHandler;
    }

    public ServerHandler getServerHandler() {
        return serverHandler;
    }

    public ChatHandler getChatHandler() {
        return chatHandler;
    }

    public String getMessage(String path) {
        return messages.getString(path, "&cMessage not found: " + path);
    }
}