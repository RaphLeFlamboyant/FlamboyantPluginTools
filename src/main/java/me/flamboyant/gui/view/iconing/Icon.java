package me.flamboyant.gui.view.iconing;

import org.bukkit.inventory.ItemStack;

public class Icon {
    private int id;
    private ItemStack itemIcon;
    private IconCallback leftClickCallback;
    private IconCallback rightClickCallback;
    private IconCallback shiftClickCallback;

    public Icon(int id) {
        this.id = id;
    }

    public Icon(int id, ItemStack itemIcon) {
        this(id);
        this.itemIcon = itemIcon;
    }

    public ItemStack getItemIcon() {
        return itemIcon;
    }

    public void setItemIcon(ItemStack itemIcon) {
        this.itemIcon = itemIcon;
    }

    public int getId() {
        return id;
    }

    public IconCallback getLeftClickCallback() {
        return leftClickCallback;
    }

    public void setLeftClickCallback(IconCallback leftClickCallback) {
        this.leftClickCallback = leftClickCallback;
    }

    public IconCallback getRightClickCallback() {
        return rightClickCallback;
    }

    public void setRightClickCallback(IconCallback rightClickCallback) {
        this.rightClickCallback = rightClickCallback;
    }

    public IconCallback getShiftClickCallback() {
        return shiftClickCallback;
    }

    public void setShiftClickCallback(IconCallback shiftClickCallback) {
        this.shiftClickCallback = shiftClickCallback;
    }
}
