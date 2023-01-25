package me.flamboyant.configurable.gui.items;

import me.flamboyant.gui.view.icons.IIconItem;
import me.flamboyant.configurable.parameters.AParameter;
import org.bukkit.inventory.ItemStack;

public abstract class AParameterItem implements IIconItem {
    protected ItemStack iconItem;
    protected String category;

    public AParameterItem(AParameter parameter) {
        this.iconItem = new ItemStack(parameter.getRepresentation());
        this.category = parameter.getCategory();
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public ItemStack getItem() {
        return iconItem;
    }
}
