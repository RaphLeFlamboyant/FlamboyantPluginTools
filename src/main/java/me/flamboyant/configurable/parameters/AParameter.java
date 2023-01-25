package me.flamboyant.configurable.parameters;

import org.bukkit.Material;

public class AParameter {
    private Material representation;
    private String parameterName;
    private String description;
    private String category;

    public AParameter(Material representation, String parameterName, String description, String category) {
        this.description = description;
        this.parameterName = parameterName;
        this.representation = representation;
        this.category = category;
    }

    public AParameter(Material representation, String parameterName, String description)
    {
        this(representation, parameterName, description, "Misc");
    }

    public Material getRepresentation() { return representation; }
    public String getParameterName() { return parameterName; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
}
