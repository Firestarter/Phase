package xyz.nkomarn.Phase.gui.handler;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import xyz.nkomarn.Phase.gui.GUIHolder;
import xyz.nkomarn.Phase.gui.inventory.FilterMenu;
import xyz.nkomarn.Phase.gui.inventory.MainMenu;
import xyz.nkomarn.Phase.gui.inventory.PublicWarps;
import xyz.nkomarn.Phase.type.Warp;
import xyz.nkomarn.Phase.util.Search;
import xyz.nkomarn.Phase.util.WarpUtil;

public class PublicWarpsHandler implements GUIHandler {
    @Override
    public void handle(Player player, int slot, InventoryClickEvent event) {
        final GUIHolder holder = (GUIHolder) event.getInventory().getHolder();
        int page = holder.getPage();

        if (slot == 39) {
            if (page <= 1) {
                new MainMenu(player);
            } else {
                new PublicWarps(player, Math.max(0, --page));
            }
        } else if (slot == 40) {
            new FilterMenu(player);
        } else if (slot == 41) {
            new PublicWarps(player, Math.max(0, ++page));
        } else {
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() != Material.AIR
                    && clickedItem.getType()
                    != Material.WHITE_STAINED_GLASS_PANE) {
                final String name = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
                if (name.trim().length() < 1) return;
                Warp warp = Search.getWarpByName(name);
                if (warp == null) return;
                WarpUtil.warpPlayer(player, warp);
            }
        }
    }
}
