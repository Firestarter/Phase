package xyz.nkomarn.Phase.gui.handler;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import xyz.nkomarn.Phase.gui.inventory.PublicWarps;

public class MainMenuHandler implements GUIHandler {
    @Override
    public void handle(Player player, int slot, InventoryClickEvent event) {
        if (slot == 10) {
            // TODO open a tutorial book guide thing
        } else if (slot == 12) {

        } else if (slot == 13) {
            new PublicWarps(player, 1);
        } else if (slot == 14) {

        }
    }
}
