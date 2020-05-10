package xyz.nkomarn.Phase.gui.handler;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import xyz.nkomarn.Phase.gui.GuiHolder;
import xyz.nkomarn.Phase.gui.inventory.FilterMenu;
import xyz.nkomarn.Phase.gui.inventory.FilteredWarps;
import xyz.nkomarn.Phase.gui.inventory.MainMenu;
import xyz.nkomarn.Phase.util.Search;
import xyz.nkomarn.Phase.util.WarpUtil;

public class FilteredWarpsHandler implements GuiHandler {
    @Override
    public void handle(Player player, int slot, InventoryClickEvent event) {
        GuiHolder holder = (GuiHolder) event.getInventory().getHolder();
        int page = holder.getPage();
        String category = holder.getData();

        if (slot == 39) {
            if (page <= 1) {
                new MainMenu(player);
            } else {
                new FilteredWarps(player, Math.max(0, --page), category);
            }
        } else if (slot == 40) {
            new FilterMenu(player);
        } else if (slot == 41) {
            new FilteredWarps(player, Math.max(0, ++page), category);
        } else {
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() != Material.AIR
                    && clickedItem.getType()
                    != Material.WHITE_STAINED_GLASS_PANE) {
                String name = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
                if (name.trim().length() < 1) return;
                Search.getWarpByName(name).ifPresent(warp -> WarpUtil.warpPlayer(player, warp));
            }
        }
    }
}
