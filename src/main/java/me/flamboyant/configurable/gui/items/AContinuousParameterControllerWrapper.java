package me.flamboyant.configurable.gui.items;

import me.flamboyant.configurable.parameters.AParameter;
import me.flamboyant.gui.view.IconController;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public abstract class AContinuousParameterControllerWrapper<T extends AParameter> extends AParameterControllerWrapper<T> {
    public AContinuousParameterControllerWrapper(T parameter, IconController controllerToWrap) {
        super(parameter, controllerToWrap);

        iconController.setLeftClickCallback(this::leftClickCallback);
        iconController.setRightClickCallback(this::rightClickCallback);
    }

    protected void updateItem() {
        ItemMeta meta = iconItem.getItemMeta();
        meta.setDisplayName(parameterName);
        meta.setLore(Arrays.asList(description, getValueFinalText()));
        iconItem.setItemMeta(meta);
    }

    protected String getValueFinalText() {
        return "" + ChatColor.BOLD + ChatColor.GREEN + getValueText();
    }

    protected abstract String getValueText();

    private void rightClickCallback(Player player) {
        doRightClickAction();
        updateItem();
    }

    private void leftClickCallback(Player player) {
        doLeftClickAction();
        updateItem();
    }

    protected abstract void doRightClickAction();
    protected abstract void doLeftClickAction();
}
