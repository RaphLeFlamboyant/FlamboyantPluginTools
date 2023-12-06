package me.flamboyant.configurable.gui.items;

import me.flamboyant.gui.view.IconController;
import me.flamboyant.configurable.parameters.AParameter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class AParameterControllerWrapper {
    protected ItemStack iconItem;
    protected String category;
    protected String description;
    protected String parameterName;
    protected Material representation;
    protected IconController iconController;

    public AParameterControllerWrapper(AParameter parameter, IconController controllerToWrap) {
        this.category = parameter.getCategory();
        this.parameterName = parameter.getParameterName();
        this.description = parameter.getDescription();
        this.representation = parameter.getRepresentation();
        this.iconController = controllerToWrap;
    }

    public IconController getController() { return iconController; }

    protected void updateItem() {
        ItemMeta meta = iconItem.getItemMeta();
        meta.setDisplayName(parameterName);
        iconItem.setItemMeta(meta);
    }
}
