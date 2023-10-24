package me.flamboyant.gui.view.common;

import me.flamboyant.gui.view.icons.IIconItem;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class InventoryGuiParameters {
    public String viewName;
    public List<Inventory> pages;
    public List<IIconItem> iconItems;
    public boolean forceAction = false;
}
