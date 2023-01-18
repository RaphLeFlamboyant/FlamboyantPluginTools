package me.flamboyant.configurable.gui.view;

import me.flamboyant.configurable.parameters.AParameter;
import me.flamboyant.utils.ILaunchablePlugin;
import me.flamboyant.configurable.gui.wrapper.ParameterWrapper;
import me.flamboyant.configurable.gui.wrapper.items.AParameterItem;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class ParametersSelectionView implements Listener {
    private static final int inventorySize = 9 * 3;
    private static ParametersSelectionView instance;
    private Inventory view;
    private HashMap<ItemStack, AParameterItem> itemsParameter = new HashMap<>();

    protected ParametersSelectionView() {
    }

    public static ParametersSelectionView getInstance() {
        if (instance == null) {
            instance = new ParametersSelectionView();
        }

        return instance;
    }

    public static String getViewID() {
        return "Paramètres de la partie";
    }

    public Inventory getViewInstance(ILaunchablePlugin plugin) {
        if (view == null) {
            List<AParameter> parameters = plugin.getParameters();
            int finalSize = inventorySize > parameters.size()
                    ? inventorySize
                    : 9 * (parameters.size() / 9 + 1);
            Bukkit.getLogger().info("Inventory siez of "+finalSize+" for a parameters number of "+parameters.size());
            Inventory myInventory = Bukkit.createInventory(null, finalSize, getViewID());

            int index = 0;
            for (AParameter parameter : parameters) {
                AParameterItem parameterItem = ParameterWrapper.wrapParameter(parameter);
                itemsParameter.put(parameterItem.getItemStack(), parameterItem);
                myInventory.setItem((finalSize / 2) - (parameters.size() / 2) + index, parameterItem.getItemStack());
                index++;
            }

            view = myInventory;
        }

        return view;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory != view) return;
        event.setCancelled(true);
        if (event.getSlotType() == InventoryType.SlotType.QUICKBAR) return;
        ItemStack clicked = event.getCurrentItem();
        if (!itemsParameter.containsKey(clicked)) return;
        if (clicked == null || clicked.getType().isAir()) return;

        ItemStack updatedItem;
        AParameterItem params = itemsParameter.get(clicked);
        if (event.isRightClick()) updatedItem = params.OnRightClick(inventory);
        else if (event.isLeftClick()) updatedItem = params.OnLeftClick(inventory);
        else return;

        itemsParameter.remove(clicked);
        itemsParameter.put(updatedItem, params);
        event.getClickedInventory().setItem(event.getSlot(), updatedItem);
    }

    public void close() {
        InventoryClickEvent.getHandlerList().unregister(this);
        itemsParameter.clear();
        if (view != null) view.clear();
        view = null;
    }
}
