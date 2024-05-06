package me.flamboyant.gui.view.iconing;

import me.flamboyant.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class IconClickHandler implements Listener {
    private HashSet<Inventory> viewPages = new HashSet<>();
    private HashSet<me.flamboyant.gui.view.iconing.Icon> icons = new HashSet<>();

    private static me.flamboyant.gui.view.iconing.IconClickHandler instance;
    public static me.flamboyant.gui.view.iconing.IconClickHandler getInstance() {
        if (instance == null) {
            instance = new me.flamboyant.gui.view.iconing.IconClickHandler();
        }

        return instance;
    }

    private IconClickHandler() {}

    public void activateIcon(List<me.flamboyant.gui.view.iconing.Icon> toActivateIcons) {
        if (icons.size() == 0) {
            Bukkit.getLogger().info("[IconClickHandler.activateIcon] Icon list size = 0, registering events");
            Common.server.getPluginManager().registerEvents(this, Common.plugin);
        }

        Bukkit.getLogger().info("[IconClickHandler.activateIcon] Adding " + toActivateIcons.size() + " icons");
        icons.addAll(toActivateIcons);
    }

    public void deactivateIcon(List<me.flamboyant.gui.view.iconing.Icon> toDeactivateIcons) {
        Bukkit.getLogger().info("[IconClickHandler.deactivateIcon] Removing " + toDeactivateIcons.size() + " icons");
        icons.removeAll(toDeactivateIcons);

        if (icons.size() == 0) {
            Bukkit.getLogger().info("[IconClickHandler.deactivateIcon] Icon list size = 0, unregistering events");
            InventoryClickEvent.getHandlerList().unregister(this);
        }
    }

    public void addValidInventories(List<Inventory> viewPages) {
        this.viewPages.addAll(viewPages);
    }

    public void removeValidInventories(List<Inventory> viewPages) {
        this.viewPages.removeAll(viewPages);
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked().getType() != EntityType.PLAYER) return;
        Player player = (Player)event.getWhoClicked();
        Bukkit.getLogger().info("[IconClickHandler] Player " + player.getDisplayName() + " clicked on an inventory");
        Inventory inventory = event.getInventory();
        if (!viewPages.contains(inventory)) return;
        Bukkit.getLogger().info("[IconClickHandler] Clicked in a valid inventory");
        event.setCancelled(true);
        if (event.getSlotType() == InventoryType.SlotType.QUICKBAR) return;
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType().isAir()) return;
        Bukkit.getLogger().info("[IconClickHandler] Clicked an inventory item");

        Optional<me.flamboyant.gui.view.iconing.Icon> opt = icons.stream().filter(i -> i.getItemIcon().equals(clicked)).findFirst();
        if (!opt.isPresent()) {
            Bukkit.getLogger().warning(event.getWhoClicked().getName() + " selected an item of type " + clicked.getType() + " in the view but didn't find matching icon for it");
            return;
        }

        Icon iconItem = opt.get();
        if (event.isRightClick())
            iconItem.getRightClickCallback().runIconAction(iconItem.getId(), player);
        if (event.isLeftClick())
            iconItem.getLeftClickCallback().runIconAction(iconItem.getId(), player);
        if (event.isShiftClick())
            iconItem.getShiftClickCallback().runIconAction(iconItem.getId(), player);
    }
}
