package me.flamboyant.gui.view.icons.impl;

import me.flamboyant.gui.view.icons.IIconItem;
import me.flamboyant.utils.PlayerCallback;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PageSwapIconItem extends CallbackIconItem {
    private Inventory targetInventory;

    public PageSwapIconItem(PlayerCallback callback, ItemStack item, Inventory targetInventory) {
        super(callback, "", item, false);

        this.targetInventory = targetInventory;
    }

    @Override
    protected void clickAction(InventoryClickEvent event) {
        super.clickAction(event);
        event.getWhoClicked().openInventory(targetInventory);
    }
}
