package xyz.nkomarn.Phase.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import xyz.nkomarn.Phase.gui.GUIHolder;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() != null && event.getInventory().getHolder()
            instanceof GUIHolder) {
            InventoryView view = event.getView();
        }
    }
}
