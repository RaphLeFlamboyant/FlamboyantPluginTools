package me.flamboyant.configurable.gui.items;

import me.flamboyant.configurable.gui.PlayerSelectionView;
import me.flamboyant.configurable.parameters.PlayerSelectionParameter;
import me.flamboyant.gui.GuiActionCallback;
import me.flamboyant.gui.view.IconController;
import me.flamboyant.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class PlayerSelectionParameterItem extends AParameterControllerWrapper {
    private PlayerSelectionParameter parameter;
    private PlayerSelectionView playerSelectionView;
    private GuiActionCallback closeViewCallback;

    public PlayerSelectionParameterItem(PlayerSelectionParameter parameter,
                                        IconController controllerToWrap,
                                        GuiActionCallback closeViewCallback) {
        super(parameter, controllerToWrap);
        this.closeViewCallback = closeViewCallback;

        ItemMeta meta = iconItem.getItemMeta();
        meta.setDisplayName(parameter.getParameterName());
        meta.setLore(Arrays.asList(parameter.getDescription()));
        iconItem.setItemMeta(meta);

        this.parameter = parameter;

        iconController.setLeftClickCallback(this::onClick);
    }

    private void onClick(Player player) {
        openPlayerSelectionView(player);
    }

    private void openPlayerSelectionView(Player player) {
        playerSelectionView = new PlayerSelectionView(parameter.getPossibleValues(), this, closeViewCallback);
        Bukkit.getScheduler().runTaskLater(Common.plugin, () -> playerSelectionView.openInventoryForPlayer(player), 1L);
    }

    public List<Player> getConcernedPlayers() { return parameter.getConcernedPlayers(); }

    // Disable player
    public void doRightClickOnPlayer(String playerName) {
        Player player = Common.server.getPlayer(playerName);
        parameter.removeConcernedPlayer(player);
    }

    // Enable player
    public void doLeftClickOnPlayer(String playerName) {
        Player player = Common.server.getPlayer(playerName);
        parameter.addConcernedPlayer(player);
    }

    public void doLeftClickAllPlayers() {
        parameter.setAllPlayers(true);
    }

    public void doRightClickAllPlayers() {
        parameter.setAllPlayers(false);
    }

    public boolean isAllPlayers() {
        return parameter.isAllPlayers();
    }
}
