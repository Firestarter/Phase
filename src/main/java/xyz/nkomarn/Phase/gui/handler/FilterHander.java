package xyz.nkomarn.Phase.gui.handler;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import xyz.nkomarn.Phase.gui.inventory.FilteredWarps;
import xyz.nkomarn.Phase.gui.inventory.PublicWarps;

public class FilterHander implements GUIHandler {
    @Override
    public void handle(Player player, int slot, InventoryClickEvent event) {
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.WHITE_STAINED_GLASS_PANE) return;
        String category = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
        if (category.equals("All")) new PublicWarps(player, 1);
        else new FilteredWarps(player, 1, category);
    }
}
