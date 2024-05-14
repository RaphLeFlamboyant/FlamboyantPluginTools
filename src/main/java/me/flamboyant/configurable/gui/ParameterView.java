package me.flamboyant.configurable.gui;

import me.flamboyant.configurable.gui.items.*;
import me.flamboyant.configurable.parameters.*;
import me.flamboyant.gui.view.IconController;
import me.flamboyant.gui.view.InventoryView;
import me.flamboyant.utils.Common;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParameterView {
    private static final String viewName = "Paramètres";
    private InventoryView wrappedView;
    private List<AParameterControllerWrapper> controllerWrappers = new ArrayList<>();

    public ParameterView(List<AParameter> parameters) {
        createView(parameters);
    }

    private void createView(List<AParameter> parameters) {
        HashMap<String, List<IconController>> categoryToControllers = new HashMap<>();

        for (AParameter parameter : parameters) {
            if (!categoryToControllers.containsKey(parameter.getCategory())) {
                categoryToControllers.put(parameter.getCategory(), new ArrayList<>());
            }

            AParameterControllerWrapper controllerWrapper = wrapParameter(parameter);
            controllerWrappers.add(controllerWrapper);
            IconController iconController = controllerWrapper.getController();
            categoryToControllers.get(parameter.getCategory()).add(iconController);
        }

        List<IconController> iconList = new ArrayList<>();

        for (String category : categoryToControllers.keySet()) {
            IconController interCategoryIcon = new IconController();

            ItemStack interCategoryItem = new ItemStack(Material.GLASS_PANE);
            ItemMeta meta = interCategoryItem.getItemMeta();
            meta.setDisplayName(category);
            interCategoryItem.setItemMeta(meta);
            interCategoryIcon.setItemIcon(interCategoryItem);

            iconList.add(interCategoryIcon);

            for (IconController controller : categoryToControllers.get(category)) {
                iconList.add(controller);
            }
        }

        wrappedView = new InventoryView(viewName, iconList);
    }

    public void openPlayerView(Player player) {
        wrappedView.openPlayerView(player);
    }

    private AParameterControllerWrapper wrapParameter(AParameter parameter) {
        if (parameter instanceof ValueOfPlayerParameter) {
            ValueOfPlayerParameter vParam = (ValueOfPlayerParameter) parameter;
            AParameterControllerWrapper subParamItem = wrapParameter(vParam.getSubParameter());

            SkullMeta skull = (SkullMeta) subParamItem.getController().getItemIcon().getItemMeta();
            skull.setOwningPlayer(Common.server.getPlayer(vParam.getPlayerName()));
            subParamItem.getController().getItemIcon().setItemMeta(skull);

            return subParamItem;
        }
        else {
            IconController controller = new IconController();

            if (parameter instanceof BooleanParameter)
                return new BooleanParameterItem((BooleanParameter) parameter, controller);
            if (parameter instanceof IntParameter) return new IntParameterItem((IntParameter) parameter, controller);
            if (parameter instanceof PlayerSelectionParameter)
                return new PlayerSelectionParameterItem((PlayerSelectionParameter) parameter, controller, this::openPlayerView);
            if (parameter instanceof SinglePlayerParameter)
                return new SinglePlayerParameterItem((SinglePlayerParameter) parameter, controller);

            return new EnumParameterItem((EnumParameter) parameter, controller);
        }
    }
}
