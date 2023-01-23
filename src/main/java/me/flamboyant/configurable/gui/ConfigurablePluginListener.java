package me.flamboyant.configurable.gui;

import me.flamboyant.configurable.gui.view.ParametersSelectionView;
import me.flamboyant.utils.ChatColorUtils;
import me.flamboyant.utils.Common;
import me.flamboyant.utils.ILaunchablePlugin;
import me.flamboyant.utils.ItemHelper;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ConfigurablePluginListener implements Listener {
    private static ConfigurablePluginListener instance;
    private ILaunchablePlugin currentlyConfiguringPlugin;
    private ItemStack[] opPlayerInventoryContent;

    protected ConfigurablePluginListener() {
    }

    public static ConfigurablePluginListener getInstance() {
        if (instance == null) {
            instance = new ConfigurablePluginListener();
        }

        return instance;
    }

    public boolean isLaunched() { return currentlyConfiguringPlugin != null; }

    public void launch(ILaunchablePlugin plugin, Player opPlayer) {
        currentlyConfiguringPlugin = plugin;
        opPlayerInventoryContent = opPlayer.getInventory().getContents();
        opPlayer.getInventory().clear();
        opPlayer.getInventory().setItem(0, getParametersItem());
        opPlayer.getInventory().setItem(5, getLaunchItem());

        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    public void openParametersView(Player player) {
        Inventory view = ParametersSelectionView.getInstance().getViewInstance(currentlyConfiguringPlugin);
        Common.server.getPluginManager().registerEvents(ParametersSelectionView.getInstance(), Common.plugin);
        player.openInventory(view);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle() != ParametersSelectionView.getViewID()) return;

        InventoryClickEvent.getHandlerList().unregister(ParametersSelectionView.getInstance());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (ItemHelper.isExactlySameItemKind(event.getItem(), getLaunchItem())) {
            event.setCancelled(true);
            launchGame(event.getPlayer());
        } else if (ItemHelper.isExactlySameItemKind(event.getItem(), getParametersItem())) {
            event.setCancelled(true);
            openParametersView(event.getPlayer());
        } else if (ItemHelper.isExactlySameItemKind(event.getItem(), getCancelItem())) {
            event.setCancelled(true);
            regivePlayerStuff(event.getPlayer());
            close(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (event.getPlayer().isOp()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        System.out.println("Swap canceled");
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().isOp()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().isOp()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        event.setCancelled(true);
    }

    private void launchGame(Player player) {
        System.out.println("Le plugin est lancé");
        player.sendMessage(ChatColorUtils.feedback("Le plugin est lancé"));
        regivePlayerStuff(player);
        currentlyConfiguringPlugin.start();
        close(player);
    }

    private void regivePlayerStuff(Player player) {
        player.getInventory().clear();
        player.getInventory().setContents(opPlayerInventoryContent);
    }

    private void close(Player player) {
        ParametersSelectionView.getInstance().close();
        currentlyConfiguringPlugin = null;
        unregister();
    }

    private void unregister() {
        PlayerInteractEvent.getHandlerList().unregister(this);
        PlayerDropItemEvent.getHandlerList().unregister(this);
        PlayerSwapHandItemsEvent.getHandlerList().unregister(this);
        BlockBreakEvent.getHandlerList().unregister(this);
        BlockPlaceEvent.getHandlerList().unregister(this);
        BlockDamageEvent.getHandlerList().unregister(this);
        EntityDamageByEntityEvent.getHandlerList().unregister(this);
    }

    private static ItemStack getParametersItem() {
        return ItemHelper.generateItem(Material.REDSTONE, 1, "Paramètres", new ArrayList<String>(), true, Enchantment.MENDING, true, true);
    }

    private static ItemStack getCancelItem() {
        return ItemHelper.generateItem(Material.ZOMBIE_HEAD, 1, "Annuler", new ArrayList<String>(), true, Enchantment.MENDING, true, true);
    }

    private static ItemStack getLaunchItem() {
        return ItemHelper.generateItem(Material.TIPPED_ARROW, 1, "Lancer la partie", new ArrayList<String>(), true, Enchantment.MENDING, true, true);
    }
}
