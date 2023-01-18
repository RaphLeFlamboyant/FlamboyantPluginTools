package me.flamboyant.configurable.gui.wrapper.items;

import me.flamboyant.configurable.parameters.AParameter;
import me.flamboyant.utils.Common;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.SkullMeta;

public class ValueOfPlayerParameterItem extends AParameterItem {
    private AParameterItem subParameterItem;
    private String playerName;

    public ValueOfPlayerParameterItem(AParameterItem subParameterItem, AParameter subParam, String playerName) {
        super(subParam);

        this.subParameterItem = subParameterItem;
        this.playerName = playerName;
    }

    @Override
    protected void updateItem() {
        super.updateItem();

        SkullMeta skull = (SkullMeta) item.getItemMeta();
        skull.setOwningPlayer(Common.server.getPlayer(playerName));
        item.setItemMeta(skull);
    }

    @Override
    protected String getValueText() {
        return subParameterItem.getValueText();
    }

    @Override
    protected void doLeftClickModification(Inventory viewClickFrom) {
        subParameterItem.doLeftClickModification(viewClickFrom);
    }

    @Override
    protected void doRightClickModification(Inventory viewClickFrom) {
        subParameterItem.doRightClickModification(viewClickFrom);
    }
}
