package me.flamboyant.configurable.gui.wrapper.items;

import me.flamboyant.configurable.parameters.IntParameter;
import org.bukkit.inventory.Inventory;

public class IntParameterItem extends AParameterItem {
    protected IntParameter parameter;

    public IntParameterItem(IntParameter parameter) {
        super(parameter);

        this.parameter = parameter;
    }

    @Override
    protected String getValueText() {
        return Integer.toString(parameter.getValue());
    }

    @Override
    protected void doLeftClickModification(Inventory viewClickFrom) {
        if (parameter.getValue() < parameter.getMaxValue())
            parameter.setValue(parameter.getValue() + 1);
    }

    @Override
    protected void doRightClickModification(Inventory viewClickFrom) {
        if (parameter.getValue() > parameter.getMinValue())
            parameter.setValue(parameter.getValue() - 1);
    }
}
