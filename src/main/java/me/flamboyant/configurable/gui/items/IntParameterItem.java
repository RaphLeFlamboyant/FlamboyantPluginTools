package me.flamboyant.configurable.gui.items;

import me.flamboyant.configurable.parameters.IntParameter;
import me.flamboyant.gui.view.IconController;

public class IntParameterItem extends AContinuousParameterControllerWrapper {
    protected IntParameter parameter;

    public IntParameterItem(IntParameter parameter, IconController controllerToWrap) {
        super(parameter, controllerToWrap);

        this.parameter = parameter;
    }

    @Override
    protected String getValueText() {
        return Integer.toString(parameter.getValue());
    }

    @Override
    protected void doLeftClickAction() {
        if (parameter.getValue() < parameter.getMaxValue())
            parameter.setValue(parameter.getValue() + 1);
    }

    @Override
    protected void doRightClickAction() {
        if (parameter.getValue() > parameter.getMinValue())
            parameter.setValue(parameter.getValue() - 1);
    }
}
