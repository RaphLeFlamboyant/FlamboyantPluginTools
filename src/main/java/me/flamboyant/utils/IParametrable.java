package me.flamboyant.utils;

import me.flamboyant.parameters.AParameter;

import java.util.List;

public interface IParametrable {
    void resetParameters();
    List<AParameter> getParameters();
}
