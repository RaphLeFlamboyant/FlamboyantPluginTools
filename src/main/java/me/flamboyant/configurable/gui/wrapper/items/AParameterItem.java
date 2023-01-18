package me.flamboyant.configurable.gui.wrapper.items;

import me.flamboyant.configurable.parameters.AParameter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public abstract class AParameterItem {
    protected ItemStack item;
    private String description;
    private String parameterName;
    private Material representation;

    public AParameterItem(AParameter parameter) {
        this.parameterName = parameter.getParameterName();
        this.description = parameter.getDescription();
        this.representation = parameter.getRepresentation();
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getDescription() {
        return description;
    }

    public ItemStack getItemStack() {
        if (item == null) {
            item = new ItemStack(representation);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(parameterName);
            item.setItemMeta(meta);
            updateItem();
        }

        return item;
    }

    public ItemStack OnLeftClick(Inventory viewClickFrom) {
        doLeftClickModification(viewClickFrom);
        updateItem();

        return item;
    }

    public ItemStack OnRightClick(Inventory viewClickFrom) {
        doRightClickModification(viewClickFrom);
        updateItem();

        return item;
    }

    protected void updateItem() {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList(description, getValueFinalText()));
        item.setItemMeta(meta);
    }

    protected String getValueFinalText() {
        return "" + ChatColor.BOLD + ChatColor.GREEN + getValueText();
    }

    protected abstract String getValueText();
    protected abstract void doLeftClickModification(Inventory viewClickFrom); // add, next
    protected abstract void doRightClickModification(Inventory viewClickFrom); // remove, previous
}
