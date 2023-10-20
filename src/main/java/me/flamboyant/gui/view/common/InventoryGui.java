package me.flamboyant.gui.view.common;

import me.flamboyant.gui.view.icons.IIconItem;
import me.flamboyant.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class InventoryGui implements Listener {
    private boolean forceAction;
    private String viewName;
    private List<IIconItem> icons;
    private List<Inventory> pages;
    private List<IInventoryGuiVisitor> visitors = new ArrayList<>();
    private HashSet<Player> playerOpeningIntentory = new HashSet<>();

    public InventoryGui(String viewName, List<Inventory> pages, List<IIconItem> icons, boolean forceAction) {
        this.pages = pages;
        this.viewName = viewName;
        this.icons = icons;
        this.forceAction = forceAction;
    }

    public void open(Player player) {
        onOpen();
        player.openInventory(pages.get(0));

        if (playerOpeningIntentory.isEmpty()) {
            Common.server.getPluginManager().registerEvents(this, Common.plugin);
        }
        playerOpeningIntentory.add(player);
    }

    public String getViewName() {
        return viewName;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (!pages.contains(inventory)) return;
        event.setCancelled(true);
        if (event.getSlotType() == InventoryType.SlotType.QUICKBAR) return;
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType().isAir()) return;
        if (!event.isRightClick() && !event.isLeftClick()) return;

        Optional<IIconItem> opt = icons.stream().filter(i -> i.getItem().equals(clicked)).findFirst();
        if (!opt.isPresent()) {
            Bukkit.getLogger().warning(event.getWhoClicked().getName() + " selected an item of type " + clicked.getType() + " in the view " + viewName + " but didn't find matching icon for it");
            return;
        }

        IIconItem iconItem = opt.get();
        iconItem.onClick(event);
        event.getClickedInventory().setItem(event.getSlot(), iconItem.getItem());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        if (event.getPlayer().getType() != EntityType.PLAYER) return;
        Player player = (Player)event.getPlayer();
        if (!pages.contains(inventory)) return;
        if (forceAction)
            Bukkit.getScheduler().runTaskLater(Common.plugin, () -> event.getPlayer().openInventory(inventory), 1);
        else
            close(player);
    }

    public void addVisitor(IInventoryGuiVisitor visitor) {
        visitors.add(visitor);
    }

    public void removeVisitor(IInventoryGuiVisitor visitor) {
        visitors.remove(visitor);
    }

    protected void onOpen() {
    }

    protected void onClose(Player player) {
        for (IInventoryGuiVisitor visitor : visitors) {
            visitor.onClose(player);
        }
    }

    private void close(Player player) {
        onClose(player);

        playerOpeningIntentory.remove(player);

        if (playerOpeningIntentory.isEmpty()) {
            InventoryClickEvent.getHandlerList().unregister(this);
            InventoryCloseEvent.getHandlerList().unregister(this);
        }
    }
}
