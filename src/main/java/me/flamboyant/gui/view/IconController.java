package me.flamboyant.gui.view;

import me.flamboyant.gui.GuiActionCallback;
import org.bukkit.inventory.ItemStack;

public class IconController {
    private static int nextIconId = 0;

    private int id;
    private ItemStack itemIcon;
    private GuiActionCallback leftClickCallback;
    private GuiActionCallback rightClickCallback;
    private GuiActionCallback shiftClickCallback;

    public IconController() {
        this.id = nextIconId++;

        leftClickCallback = a -> {};
        rightClickCallback = a -> {};
        shiftClickCallback = a -> {};
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

    public GuiActionCallback getLeftClickCallback() {
        return leftClickCallback;
    }

    public void setLeftClickCallback(GuiActionCallback leftClickCallback) {
        this.leftClickCallback = leftClickCallback;
    }

    public GuiActionCallback getRightClickCallback() {
        return rightClickCallback;
    }

    public void setRightClickCallback(GuiActionCallback rightClickCallback) {
        this.rightClickCallback = rightClickCallback;
    }

    public GuiActionCallback getShiftClickCallback() {
        return shiftClickCallback;
    }

    public void setShiftClickCallback(GuiActionCallback shiftClickCallback) {
        this.shiftClickCallback = shiftClickCallback;
    }
}
