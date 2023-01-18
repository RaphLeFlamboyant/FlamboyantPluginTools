package me.flamboyant.configurable.gui.wrapper;

import me.flamboyant.configurable.gui.wrapper.items.*;
import me.flamboyant.configurable.parameters.*;

public class ParameterWrapper {
    public static AParameterItem wrapParameter(AParameter parameter) {
        if (parameter instanceof ValueOfPlayerParameter) {
            ValueOfPlayerParameter vParam = (ValueOfPlayerParameter) parameter;
            AParameterItem subParamItem = wrapParameter(vParam.getSubParameter());

            return new ValueOfPlayerParameterItem(subParamItem, vParam.getSubParameter(), vParam.getPlayerName());
        }
        if (parameter instanceof SectionParameter) return new SectionParameterItem(parameter);
        if (parameter instanceof BooleanParameter) return new BooleanParameterItem((BooleanParameter) parameter);
        if (parameter instanceof IntParameter) return new IntParameterItem((IntParameter) parameter);
        if (parameter instanceof PlayerSelectionParameter) return new PlayerSelectionParameterItem((PlayerSelectionParameter) parameter);
        if (parameter instanceof SinglePlayerParameter) return new SinglePlayerParameterItem((SinglePlayerParameter) parameter);
        return new EnumParameterItem((EnumParameter) parameter);
    }
}
