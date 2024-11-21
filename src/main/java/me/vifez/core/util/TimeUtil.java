package me.vifez.core.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtil {

    public static long parseTime(String arg) {
        long totalTime = 0L;
        boolean found = false;
        Matcher matcher = Pattern.compile("\\d+\\D+").matcher(arg);

        while (matcher.find()) {
            String s = matcher.group();
            long value = Long.parseLong(s.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[0]);
            String type = s.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[1];

            switch (type) {
                case "s":
                    totalTime += value;
                    found = true;
                    break;
                case "m":
                    totalTime += value * 60;
                    found = true;
                    break;
                case "h":
                    totalTime += value * 60 * 60;
                    found = true;
                    break;
                case "d":
                    totalTime += value * 60 * 60 * 24;
                    found = true;
                    break;
                case "w":
                    totalTime += value * 60 * 60 * 24 * 7;
                    found = true;
                    break;
                case "M":
                    totalTime += value * 60 * 60 * 24 * 30;
                    found = true;
                    break;
                case "y":
                    totalTime += value * 60 * 60 * 24 * 365;
                    found = true;
                    break;
            }
        }

        return !found ? -1 : totalTime * 1000;
    }

    public static String formatDate(long millis) {
        Date date = new Date(millis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return new SimpleDateFormat("MMM dd yyyy (hh:mm aa zz)").format(date);
    }

    public static String format(long time) {
        String format = "";

        int days = 0;
        while (time >= TimeUnit.DAYS.toSeconds(1)) {
            days++;
            time -= TimeUnit.DAYS.toSeconds(1);
        }

        int hours = 0;
        while (time >= TimeUnit.HOURS.toSeconds(1)) {
            hours++;
            time -= TimeUnit.HOURS.toSeconds(1);
        }

        int minutes = 0;
        while (time >= TimeUnit.MINUTES.toSeconds(1)) {
            minutes++;
            time -= TimeUnit.MINUTES.toSeconds(1);
        }

        if (days > 0) {
            format += days + "d" + (hours > 0 ? ", " : "");
        }

        if (hours > 0) {
            format += hours + "h" + (minutes > 0 ? ", " : "");
        }

        if (minutes > 0) {
            format += minutes + "m" + (time > 0 ? ", " : "");
        }

        if (time > 0) {
            format += time + "s";
        }

        return format.isEmpty() ? "0s" : format;
    }

}
