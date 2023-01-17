package me.flamboyant.utils;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChatColorUtils {
    public static final List<ChatColor> chatColorValues = Collections.unmodifiableList(Arrays.asList(ChatColor.values()));
    public static final int chatColorValuesSize = chatColorValues.size();

    public static String advertisementMessage(String message) {
        return "" + ChatColor.GOLD + ChatColor.ITALIC + message;
    }

    public static String feedback(String message) {
        return ChatColor.GREEN + "¤" + ChatColor.RESET + message;
    }
}
