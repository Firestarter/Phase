package xyz.nkomarn.Phase.listener;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import xyz.nkomarn.Phase.gui.GuiHolder;
import xyz.nkomarn.Phase.gui.GuiType;
import xyz.nkomarn.Phase.gui.handler.*;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() == null || !(event.getInventory().getHolder() instanceof GuiHolder)) return;
        if (!(event.getRawSlot() < event.getInventory().getSize())) return; // TODO ignore number keys selection

        int slot = event.getSlot();
        InventoryView view = event.getView();

        if (slot < 0) return;
        if (slot == view.convertSlot(slot)) {
            event.setCancelled(true);
            GuiHolder holder = (GuiHolder) event.getInventory().getHolder();
            GuiType type = holder.getType();
            Player player = (Player) event.getWhoClicked();

            GuiHandler handler = null;
            if (type == GuiType.MAIN_MENU) handler = new MainMenuHandler();
            else if (type == GuiType.FEATURED_WARPS) handler = new FeaturedWarpsHandler();
            else if (type == GuiType.PUBLIC_WARPS) handler = new PublicWarpsHandler();
            else if (type == GuiType.PLAYER_WARPS) handler = new PlayerWarpsHandler();
            else if (type == GuiType.FILTERED_WARPS) handler = new FilteredWarpsHandler();
            else if (type == GuiType.FILTER) handler = new FilterHander();
            else if (type == GuiType.CATEGORY_PICKER) handler = new CategoryPickerHandler();

            if (handler != null) {
                handler.handle(player, slot, event);
                player.playSound(player.getLocation(), Sound.UI_LOOM_TAKE_RESULT, 0.6f, 0.9f);
            }
        }
    }
}
