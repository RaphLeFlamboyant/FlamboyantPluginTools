package me.flamboyant.configurable.gui;

import me.flamboyant.configurable.gui.items.PlayerSelectionParameterItem;
import me.flamboyant.gui.GuiActionCallback;
import me.flamboyant.gui.view.IconController;
import me.flamboyant.gui.view.InventoryView;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class PlayerSelectionView {
    private static final String viewName = "Sélection de joueur";
    private static final String allPlayers = "Tous les joueurs";
    private static final String backToMenu = "Retour au menu";
    private InventoryView wrappedView;
    private PlayerSelectionParameterItem parameterItem;
    private HashMap<String, IconController> playerNameToController;
    private IconController allPlayersIconController;
    private IconController backToMenuIconController;
    private GuiActionCallback closeCallback;

    public PlayerSelectionView(Player[] players, PlayerSelectionParameterItem parameterItem, GuiActionCallback closeCallback) {
        this.closeCallback = closeCallback;
        this.parameterItem = parameterItem;
        playerNameToController = new HashMap<>();

        createView(players);
    }

    public void openInventoryForPlayer(Player player) {
        wrappedView.openPlayerView(player);
    }

    private void createView(Player[] players) {
        List<IconController> iconList = new ArrayList<>();

        int id = 1;
        for (Player player : players) {
            ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skull = (SkullMeta) playerHead.getItemMeta();
            skull.setDisplayName(player.getDisplayName());
            skull.setLore(Arrays.asList(getValueString(false)));
            skull.setOwningPlayer(player);
            playerHead.setItemMeta(skull);
            if (parameterItem.getConcernedPlayers().contains(player))
                addItemSelectionVisual(playerHead);

            IconController controller = new IconController(id++);
            controller.setItemIcon(playerHead);
            controller.setLeftClickCallback((p) -> onPlayerLeftClick(player.getDisplayName()));
            controller.setRightClickCallback((p) -> onPlayerRightClick(player.getDisplayName()));

            iconList.add(controller);
            playerNameToController.put(player.getDisplayName(), controller);
        }

        ItemStack allPlayersItem = new ItemStack(Material.LEVER);
        ItemMeta meta = allPlayersItem.getItemMeta();
        meta.setDisplayName(allPlayers);
        meta.setLore(Arrays.asList(getValueString(false)));
        allPlayersItem.setItemMeta(meta);

        IconController controller = new IconController(id++);
        controller.setItemIcon(allPlayersItem);
        controller.setLeftClickCallback((p) -> onAllPlayerLeftClick());
        controller.setRightClickCallback((p) -> onAllPlayerRightClick());
        allPlayersIconController = controller;
        if (parameterItem.isAllPlayers()) onAllPlayerLeftClick();

        ItemStack previous = new ItemStack(Material.REDSTONE);
        previous.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
        meta = previous.getItemMeta();
        meta.setDisplayName(backToMenu);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        previous.setItemMeta(meta);

        controller = new IconController(id++);
        controller.setItemIcon(previous);
        controller.setLeftClickCallback((p) -> p.closeInventory());
        backToMenuIconController = controller;

        wrappedView = new InventoryView(viewName, iconList);
        wrappedView.addCloseViewListener(this::onInventoryClosed);
    }

    private void onInventoryClosed(Player player) {
        playerNameToController.clear();
        allPlayersIconController = null;
        backToMenuIconController = null;
        wrappedView = null;
        closeCallback.runAction(player);
        closeCallback = null;
    }

    private void onPlayerLeftClick(String playerClickedName) {
        parameterItem.doLeftClickOnPlayer(playerClickedName);
        addItemSelectionVisual(playerNameToController.get(playerClickedName).getItemIcon());
    }

    private void onPlayerRightClick(String playerClickedName) {
        parameterItem.doRightClickOnPlayer(playerClickedName);
        removeItemSelectionVisual(playerNameToController.get(playerClickedName).getItemIcon());
        removeItemSelectionVisual(allPlayersIconController.getItemIcon());
    }

    private void onAllPlayerLeftClick() {
        parameterItem.doLeftClickAllPlayers();
        addItemSelectionVisual(allPlayersIconController.getItemIcon());

        for (String playerName : playerNameToController.keySet()) {
            addItemSelectionVisual(playerNameToController.get(playerName).getItemIcon());
            parameterItem.doLeftClickOnPlayer(playerName);
        }
    }

    private void onAllPlayerRightClick() {
        parameterItem.doRightClickAllPlayers();
        removeItemSelectionVisual(allPlayersIconController.getItemIcon());

        for (String playerName : playerNameToController.keySet()) {
            removeItemSelectionVisual(playerNameToController.get(playerName).getItemIcon());
            parameterItem.doRightClickOnPlayer(playerName);
        }
    }

    private void addItemSelectionVisual(ItemStack item) {
        item.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setLore(Arrays.asList(getValueString(true)));
        item.setItemMeta(meta);
    }

    private void removeItemSelectionVisual(ItemStack item) {
        item.removeEnchantment(Enchantment.ARROW_FIRE);
    }

    private String getValueString(boolean value) {
        return value ? "YES" : "NO";
    }
}
