package me.flamboyant.configurable.gui.wrapper.items;

import me.flamboyant.configurable.parameters.AParameter;
import org.bukkit.inventory.Inventory;

public class SectionParameterItem extends AParameterItem {
    public SectionParameterItem(AParameter parameter) {
        super(parameter);
    }

    @Override
    protected String getValueText() {
        return "";
    }

    @Override
    protected void doLeftClickModification(Inventory viewClickFrom) {

    }

    @Override
    protected void doRightClickModification(Inventory viewClickFrom) {

    }
}
