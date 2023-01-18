package me.flamboyant.configurable.gui.wrapper.items;

import me.flamboyant.configurable.parameters.StringSelectionParameter;
import org.bukkit.inventory.Inventory;

public class StringSelectionParameterItem extends AParameterItem {
    private int index = 0;
    private StringSelectionParameter parameter;

    public StringSelectionParameterItem(StringSelectionParameter parameter) {
        super(parameter);

        this.parameter = parameter;
        while (parameter.getPossibleValues()[index] != parameter.getSelectedValue()) index++;
    }

    @Override
    protected String getValueText() {
        return parameter.getPossibleValues()[index];
    }

    @Override
    protected void doLeftClickModification(Inventory viewClickFrom) {
        if (++index == parameter.getPossibleValues().length) index = 0;
        parameter.setSelectedValue(parameter.getPossibleValues()[index]);
    }

    @Override
    protected void doRightClickModification(Inventory viewClickFrom) {
        if (--index < 0) index = parameter.getPossibleValues().length - 1;
        parameter.setSelectedValue(parameter.getPossibleValues()[index]);
    }
}
