package me.flamboyant.configurable.parameters;

import org.bukkit.Material;

public class SectionParameter extends AParameter {
    public SectionParameter(String parameterName, String description) {
        super(Material.GLASS_PANE, parameterName, description);
    }
}
