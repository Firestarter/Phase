package xyz.nkomarn.Phase.listener;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import xyz.nkomarn.Phase.gui.GUIHolder;
import xyz.nkomarn.Phase.gui.GUIType;
import xyz.nkomarn.Phase.gui.handler.*;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() == null || !(event.getInventory().getHolder() instanceof GUIHolder)) return;
        if (!(event.getRawSlot() < event.getInventory().getSize())) return; // TODO ignore number keys selection

        final int slot = event.getSlot();
        final InventoryView view = event.getView();

        if (slot < 0) return;
        if (slot == view.convertSlot(slot)) {
            event.setCancelled(true);
            final GUIHolder holder = (GUIHolder) event.getInventory().getHolder();
            final GUIType type = holder.getType();
            final String category = holder.getData();
            final int page = holder.getPage();
            final Player player = (Player) event.getWhoClicked();

            GUIHandler handler = null;
            if (type == GUIType.MAIN_MENU) handler = new MainMenuHandler();
            else if (type == GUIType.FEATURED_WARPS) handler = new FeaturedWarpsHandler();
            else if (type == GUIType.PUBLIC_WARPS) handler = new PublicWarpsHandler();
            else if (type == GUIType.PLAYER_WARPS) handler = new PlayerWarpsHandler();
            else if (type == GUIType.OPTIONS) handler = new OptionsHandler();
            else if (type == GUIType.FAVORITES) handler = new FavoriteWarpsHandler();

            if (handler != null) {
                handler.handle(player, slot, event);
                player.playSound(player.getLocation(), Sound.UI_LOOM_TAKE_RESULT, 1.0f, 1.0f);
            }
        }
    }
}
