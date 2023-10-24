package me.flamboyant.gui.view.common;

import me.flamboyant.gui.view.icons.IIconItem;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class InventoryGuiParameters {
    public String ViewName;
    public List<Inventory> Pages;
    public List<IIconItem> Icons;
    public boolean ForceAction = false;
}
