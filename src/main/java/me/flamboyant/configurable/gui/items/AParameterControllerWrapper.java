package me.flamboyant.configurable.gui.items;

import me.flamboyant.gui.view.IconController;
import me.flamboyant.configurable.parameters.AParameter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public abstract class AParameterControllerWrapper<T extends AParameter> {
    protected ItemStack iconItem;
    protected String category;
    protected String description;
    protected String parameterName;
    protected Material representation;

    protected T parameter;
    protected IconController iconController;

    public AParameterControllerWrapper(T parameter, IconController controllerToWrap) {
        this.category = parameter.getCategory();
        this.parameterName = parameter.getParameterName();
        this.description = parameter.getDescription();
        this.representation = parameter.getRepresentation();
        this.parameter = parameter;
        this.iconController = controllerToWrap;

        finishDataInitialization();

        iconItem = new ItemStack(representation);
        updateItem();
        iconController.setItemIcon(iconItem);
    }

    public IconController getController() { return iconController; }

    protected void finishDataInitialization() {}

    protected void updateItem() {
        ItemMeta meta = iconItem.getItemMeta();
        meta.setDisplayName(parameterName);
        meta.setLore(Arrays.asList(description));
        iconItem.setItemMeta(meta);
    }
}
