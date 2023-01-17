package me.flamboyant.parameters;

import org.bukkit.Material;

public class AParameter {
    private Material representation;
    private String parameterName;
    private String description;

    public AParameter(Material representation, String parameterName, String description) {
        this.description = description;
        this.parameterName = parameterName;
        this.representation = representation;
    }

    public Material getRepresentation() { return representation; }
    public String getParameterName() { return parameterName; }
    public String getDescription() { return description; }
    public void setParameterName(String newParameterName) { parameterName = newParameterName; }
    public void setDescription(String newDescription) { description = newDescription; }
}
