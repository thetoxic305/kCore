package me.vifez.core.util;

import org.apache.commons.lang.StringUtils;

import java.util.List;

public class StringUtil {

    public static String listToString(List<String> strings) {
        return StringUtils.join(strings, "\n");
    }

}
