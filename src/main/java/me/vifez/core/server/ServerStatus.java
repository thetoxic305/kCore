package me.vifez.core.server;

public enum ServerStatus {

    ONLINE("&aOnline"),
    OFFLINE("&cOffline"),
    WHITELISTED("&dWhitelisted");

    private String text;

    ServerStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}