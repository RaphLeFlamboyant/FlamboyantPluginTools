package me.flamboyant.configurable.gui.items;

import me.flamboyant.configurable.parameters.BooleanParameter;
import me.flamboyant.gui.view.IconController;

public class BooleanParameterItem extends IntParameterItem {
    public BooleanParameterItem(BooleanParameter parameter, IconController controllerToWrap) {
        super(parameter, controllerToWrap);
    }

    @Override
    protected String getValueText() {
        return parameter.getValue() == 0 ? "NO" : "YES";
    }
}
