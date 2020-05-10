package xyz.nkomarn.Phase.gui.handler;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface GuiHandler {
    void handle(Player player, int slot, InventoryClickEvent event);
}
