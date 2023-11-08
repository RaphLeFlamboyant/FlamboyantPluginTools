package me.flamboyant.gui.view.common;

import org.bukkit.entity.Player;

public interface IInventoryGui {
    public void open(Player player);
    public void close(Player player);
    public String getViewName();
    public void addVisitor(IInventoryGuiVisitor visitor);
    public void removeVisitor(IInventoryGuiVisitor visitor);
}
