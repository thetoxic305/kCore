package me.vifez.core.util;

public class WoolColor {

    public static short fromChatColor(String color) {

        if (color.toLowerCase().contains("&0") || color.toLowerCase().contains("§0")) {
            return 15;
        }

        if (color.toLowerCase().contains("&1") || color.toLowerCase().contains("§1")) {
            return 11;
        }

        if (color.toLowerCase().contains("&2") || color.toLowerCase().contains("§2")) {
            return 13;
        }

        if (color.toLowerCase().contains("&3") || color.toLowerCase().contains("§3")) {
            return 9;
        }

        if (color.toLowerCase().contains("&c") || color.toLowerCase().contains("§c") || color.toLowerCase().contains("&4") || color.toLowerCase().contains("§4")) {
            return 14;
        }

        if (color.toLowerCase().contains("&5") || color.toLowerCase().contains("§5")) {
            return 10;
        }

        if (color.toLowerCase().contains("&6") || color.toLowerCase().contains("§6")) {
            return 1;
        }

        if (color.toLowerCase().contains("&7") || color.toLowerCase().contains("§7")) {
            return 8;
        }

        if (color.toLowerCase().contains("&8") || color.toLowerCase().contains("§8")) {
            return 7;
        }

        if (color.toLowerCase().contains("&9") || color.toLowerCase().contains("§9") || color.toLowerCase().contains("&b") || color.toLowerCase().contains("§b")) {
            return 3;
        }

        if (color.toLowerCase().contains("&a") || color.toLowerCase().contains("§a")) {
            return 5;
        }

        if (color.toLowerCase().contains("&d") || color.toLowerCase().contains("§d")) {
            return 2;
        }

        if (color.toLowerCase().contains("&e") || color.toLowerCase().contains("§e")) {
            return 4;
        }

        return 0;
    }

}