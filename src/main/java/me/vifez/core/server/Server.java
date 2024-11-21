package me.vifez.core.server;

import me.vifez.core.util.Timer;

public class Server {

    private final String name;
    private ServerStatus status;
    private int onlinePlayers;
    private int maxPlayers;
    private int vanishedPlayers;
    private double[] tps;

    private Timer lastUpdate = new Timer(0L, false);

    public Server(String name) {
        this(name, ServerStatus.ONLINE, 0, 0, 0, new double[]{20, 20, 20, 20});
    }

    public Server(String name, ServerStatus status, int onlinePlayers, int maxPlayers, int vanishedPlayers, double[] tps) {
        this.name = name;
        this.status = status;
        this.onlinePlayers = onlinePlayers;
        this.maxPlayers = maxPlayers;
        this.vanishedPlayers = vanishedPlayers;
        this.tps = tps;

        lastUpdate.reset();
    }

    public String getName() {
        return name;
    }

    public ServerStatus getStatus() {
        return status;
    }

    public void setStatus(ServerStatus status) {
        this.status = status;
    }

    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    public void setOnlinePlayers(int onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getVanishedPlayers() {
        return vanishedPlayers;
    }

    public void setVanishedPlayers(int vanishedPlayers) {
        this.vanishedPlayers = vanishedPlayers;
    }

    public double[] getTPS() {
        return tps;
    }

    public void setTPS(double[] tps) {
        this.tps = tps;
    }

    public Timer getLastUpdate() {
        return lastUpdate;
    }

}