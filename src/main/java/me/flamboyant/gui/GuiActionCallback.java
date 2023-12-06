package me.flamboyant.gui;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface GuiActionCallback {
    void runAction(Player player);
}
