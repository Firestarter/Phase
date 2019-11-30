package xyz.nkomarn.Phase.gui.handler;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface GUIHandler {
    void handle(final Player player, final int slot, final InventoryClickEvent event);
}
