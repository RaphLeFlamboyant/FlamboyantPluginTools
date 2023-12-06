package me.flamboyant.gui.view.iconing;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface IconCallback {
    void runIconAction(int iconId, Player player);
}
