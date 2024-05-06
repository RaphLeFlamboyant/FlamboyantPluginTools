package me.flamboyant.configurable.gui.items;

import me.flamboyant.configurable.parameters.EnumParameter;
import me.flamboyant.gui.view.IconController;

public class EnumParameterItem extends AContinuousParameterControllerWrapper<EnumParameter> {
    private int index = 0;

    public EnumParameterItem(EnumParameter parameter, IconController controllerToWrap) {
        super(parameter, controllerToWrap);
    }

    @Override
    public void finishDataInitialization() {
        while (parameter.getPossibleValues()[index] != parameter.getSelectedValue()) index++;
    }

    @Override
    protected String getValueText() {
        return index >= 0
                ? parameter.getPossibleValues()[index].toString()
                : "NONE";
    }

    @Override
    protected void doLeftClickAction() {
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
    protected void doRightClickAction() {
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
