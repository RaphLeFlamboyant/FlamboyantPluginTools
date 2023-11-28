package me.flamboyant.gui.view;

import me.flamboyant.gui.GuiActionCallback;
import me.flamboyant.gui.view.icon.Icon;
import me.flamboyant.gui.view.icon.IconClickHandler;
import me.flamboyant.utils.Common;
import me.flamboyant.utils.ItemHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class InventoryView implements Listener {
    private static final int inventorySize = 6 * 6;
    private static final int pageSize = 5 * 6;
    private static final int nextPageIconId = -1;
    private static final int previousPageIconId = -2;

    private String viewName;
    private HashMap<Integer, Icon> idToIcon = new HashMap<>();
    private HashMap<Integer, IconController> idToController = new HashMap<>();
    private List<Inventory> pages = new ArrayList<>();
    private HashMap<Player, Integer> playerToPageIndex = new HashMap<>();

    // updatable (necessary to link updates request to view update)
    private ArrayList<Icon> builtIconList = new ArrayList<>();
    private HashMap<Integer, Integer> iconIdToIconListIndex = new HashMap<>();

    // HAndling inventory close
    HashSet<GuiActionCallback> inventoryViewCloseCallback = new HashSet<>();

    public InventoryView(String viewName, List<IconController> iconControllers) {
        this.viewName = viewName;
        initializeFrom(iconControllers);
    }

    public void initializeFrom(List<IconController> iconControllers) {
        reset();

        Inventory currentInventory = Bukkit.createInventory(null, inventorySize, viewName);
        pages.add(currentInventory);

        int index = 0;
        for (IconController iconController : iconControllers) {
            Icon icon = createIcon(iconController);
            addIcon(icon);
            idToIcon.put(iconController.getId(), icon);

            if (index == inventorySize - 9) {
                Inventory nextInventory = Bukkit.createInventory(null, inventorySize, viewName);
                pages.add(nextInventory);

                placeNavigationIcons(currentInventory, nextInventory);

                currentInventory = nextInventory;
                index = 0;
            }

            currentInventory.setItem(index++, iconController.getItemIcon());
        }

        refreshPlayersView();
    }

    public void openPlayerView(Player player) {
        if (playerToPageIndex.containsKey(player)) {
            Bukkit.getLogger().warning("The player " + player.getDisplayName() + " has already the view " + viewName + " open.");
            return;
        }

        if (playerToPageIndex.isEmpty()) {
            Common.server.getPluginManager().registerEvents(this, Common.plugin);
            IconClickHandler.getInstance().addValidInventories(pages);
            IconClickHandler.getInstance().activateIcon(builtIconList);
        }

        playerToPageIndex.put(player, 0);
        player.openInventory(pages.get(0));
    }

    public void closePlayerView(Player player) {
        if (!playerToPageIndex.containsKey(player)) {
            Bukkit.getLogger().warning("The player " + player.getDisplayName() + " doesn't have the view " + viewName + " open.");
            return;
        }

        playerToPageIndex.remove(player);
        player.closeInventory();

        if (playerToPageIndex.isEmpty()) {
            InventoryCloseEvent.getHandlerList().unregister(this);
            IconClickHandler.getInstance().deactivateIcon(builtIconList);
            IconClickHandler.getInstance().removeValidInventories(pages);
        }
    }

    public void updateIcon(IconController iconController)
    {
        Icon icon = idToIcon.get(iconController.getId());
        IconClickHandler.getInstance().deactivateIcon(Arrays.asList(icon));
        Icon replacingIcon = createIcon(iconController);
        IconClickHandler.getInstance().activateIcon(Arrays.asList(replacingIcon));

        idToIcon.put(iconController.getId(), replacingIcon);
        idToController.put(iconController.getId(), iconController);
        int index = iconIdToIconListIndex.get(iconController.getId());
        builtIconList.set(index, icon);

        pages.get(index / pageSize).setItem(index % pageSize, iconController.getItemIcon());
    }

    @EventHandler
    private void onInventoryClose(InventoryCloseEvent event) {
        if (!playerToPageIndex.containsKey(event.getPlayer())) return;
        if (!pages.contains(event.getInventory())) return;
        // index is changed when navigating so it means the player didn't close on purpose
        if (event.getInventory() != pages.get(playerToPageIndex.get(event.getPlayer()))) return;
        event.getPlayer().getOpenInventory().getTopInventory()
    }

    private void addIcon(Icon icon) {
        iconIdToIconListIndex.put(icon.getId(), builtIconList.size());
        builtIconList.add(icon);
    }

    private void insertIcon(IconController iconController, int index) {
        if (builtIconList.size() % pageSize == 0) {
            Inventory lastInventory = pages.get(pages.size() - 1);
            Inventory newInventory = Bukkit.createInventory(null, inventorySize, viewName);
            pages.add(newInventory);

            placeNavigationIcons(lastInventory, newInventory);
        }

        Icon icon = createIcon(iconController);
        builtIconList.add(index, icon);
        idToIcon.put(iconController.getId(), icon);
        idToController.put(iconController.getId(), iconController);

        for (int i = index; i < builtIconList.size(); i++) {
            icon = builtIconList.get(i);
            iconIdToIconListIndex.put(icon.getId(), i);
            pages.get(i / pageSize).setItem(i % pageSize, icon.getItemIcon());
        }
    }

    private void deleteIcon(IconController iconController) {
        for (int i = iconIdToIconListIndex.get(iconController.getId()); i < builtIconList.size() - 1; i++) {
            Icon replacement = builtIconList.get(i + 1);
            builtIconList.set(i, replacement);
            iconIdToIconListIndex.put(replacement.getId(), i);
            pages.get(i / pageSize).setItem(i % pageSize, replacement.getItemIcon());
        }

        int lastIndex = builtIconList.size() - 1;
        pages.get(lastIndex / pageSize).setItem(lastIndex % pageSize, new ItemStack(Material.AIR));
        builtIconList.remove(lastIndex);

        idToIcon.remove(iconController.getId());
        idToController.remove(iconController.getId());
        iconIdToIconListIndex.remove(iconController.getId());

        if (builtIconList.size() % pageSize != 0) {
            return;
        }

        pages.remove(pages.size() - 1);
        Inventory lastPage = pages.get(pages.size() - 1);

        for (Player player : playerToPageIndex.keySet()) {
            int playerPageIndex = playerToPageIndex.get(player);

            if (playerPageIndex == pages.size()) {
                playerToPageIndex.put(player, playerPageIndex - 1);
                player.openInventory(lastPage);
            }
        }
    }

    private Icon createIcon(IconController iconController) {
        Icon res = new Icon(iconController.getId(), iconController.getItemIcon().clone());
        res.setLeftClickCallback(this::onLeftClick);
        res.setRightClickCallback(this::onRightClick);
        res.setShiftClickCallback(this::onShiftClick);
        return res;
    }

    private void refreshPlayersView() {
        for (Player player : playerToPageIndex.keySet()) {
            int playerPageIndex = playerToPageIndex.get(player);
            if (playerPageIndex >= playerToPageIndex.size()) {
                playerPageIndex = playerToPageIndex.size() - 1;
                playerToPageIndex.put(player, playerPageIndex);
            }

            player.openInventory(pages.get(playerPageIndex));
        }
    }

    private void reset() {
        pages.clear();
        idToController.clear();
        idToIcon.clear();
        builtIconList.clear();
        iconIdToIconListIndex.clear();
        createNavigationIcons();
    }

    private void placeNavigationIcons(Inventory currentInventory, Inventory nextInventory) {
        Icon nextIcon = idToIcon.get(nextPageIconId);
        Icon previousIcon = idToIcon.get(previousPageIconId);

        currentInventory.setItem(inventorySize - 1, nextIcon.getItemIcon());
        nextInventory.setItem(inventorySize - 9, previousIcon.getItemIcon());
    }

    private void createNavigationIcons() {
        Icon goToNextPageIcon = new Icon(nextPageIconId);
        goToNextPageIcon.setLeftClickCallback((i, p) -> goToNextPage(p));
        goToNextPageIcon.setItemIcon(ItemHelper.generateItem(Material.PAPER, 1, "Page suivante", Arrays.asList(), false, null, true, false));
        idToIcon.put(nextPageIconId, goToNextPageIcon);

        Icon goToPreviousPageIcon = new Icon(previousPageIconId);
        goToPreviousPageIcon.setLeftClickCallback((i, p) -> goToPreviousPage(p));
        goToPreviousPageIcon.setItemIcon(ItemHelper.generateItem(Material.PAPER, 1, "Page précédente", Arrays.asList(), false, null, true, false));
        idToIcon.put(previousPageIconId, goToPreviousPageIcon);
    }

    private void goToNextPage(Player player) {
        int playerPageIndex = playerToPageIndex.get(player) + 1;
        playerToPageIndex.put(player, playerPageIndex);
        player.openInventory(pages.get(playerPageIndex));
    }

    private void goToPreviousPage(Player player) {
        int playerPageIndex = playerToPageIndex.get(player) - 1;
        playerToPageIndex.put(player, playerPageIndex);
        player.openInventory(pages.get(playerPageIndex));
    }

    private void onLeftClick(int iconId, Player player) {
        idToController.get(iconId).getLeftClickCallback().runAction(player);
    }

    private void onRightClick(int iconId, Player player) {
        idToController.get(iconId).getRightClickCallback().runAction(player);
    }

    private void onShiftClick(int iconId, Player player) {
        idToController.get(iconId).getShiftClickCallback().runAction(player);
    }
}
