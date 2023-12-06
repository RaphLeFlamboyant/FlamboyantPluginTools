package me.flamboyant.configurable.gui.items;

import me.flamboyant.configurable.parameters.StringSelectionParameter;
import me.flamboyant.gui.view.IconController;

public class StringSelectionParameterItem extends AContinuousParameterControllerWrapper {
    private int index = 0;
    private StringSelectionParameter parameter;

    public StringSelectionParameterItem(StringSelectionParameter parameter, IconController controllerToWrap) {
        super(parameter, controllerToWrap);

        this.parameter = parameter;
        while (parameter.getPossibleValues()[index] != parameter.getSelectedValue()) index++;
    }

    @Override
    protected String getValueText() {
        return parameter.getPossibleValues()[index];
    }

    @Override
    protected void doLeftClickAction() {
        if (++index == parameter.getPossibleValues().length) index = 0;
        parameter.setSelectedValue(parameter.getPossibleValues()[index]);
    }

    @Override
    protected void doRightClickAction() {
        if (--index < 0) index = parameter.getPossibleValues().length - 1;
        parameter.setSelectedValue(parameter.getPossibleValues()[index]);
    }
}
