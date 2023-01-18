package me.flamboyant.configurable.gui.wrapper.items;

import me.flamboyant.configurable.parameters.EnumParameter;
import org.bukkit.inventory.Inventory;

public class EnumParameterItem extends AParameterItem {
    private int index = 0;
    private EnumParameter parameter;

    public EnumParameterItem(EnumParameter parameter) {
        super(parameter);

        this.parameter = parameter;
        while (parameter.getPossibleValues()[index] != parameter.getSelectedValue()) index++;
    }

    @Override
    protected String getValueText() {
        return parameter.getPossibleValues()[index].toString();
    }

    @Override
    protected void doLeftClickModification(Inventory viewClickFrom) {
        if (++index == parameter.getPossibleValues().length) {
            if (parameter.isNullable()) {
                index = -1;
                parameter.setSelectedValue(null);
                return;
            }

            index = 0;
        }
        parameter.setSelectedValue(parameter.getPossibleValues()[index]);
    }

    @Override
    protected void doRightClickModification(Inventory viewClickFrom) {
        if (--index < 0) {
            if (index == -1 && parameter.isNullable()) {
                parameter.setSelectedValue(null);
                return;
            }

            index = parameter.getPossibleValues().length - 1;
        }
        parameter.setSelectedValue(parameter.getPossibleValues()[index]);
    }
}
