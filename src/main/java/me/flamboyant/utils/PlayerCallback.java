package me.flamboyant.utils;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface PlayerCallback {
    void runOnPlayer(Player player);
}
