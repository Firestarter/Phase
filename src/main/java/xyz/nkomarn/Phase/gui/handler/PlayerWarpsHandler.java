package xyz.nkomarn.Phase.gui.handler;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import xyz.nkomarn.Phase.gui.GUIHolder;
import xyz.nkomarn.Phase.gui.inventory.*;
import xyz.nkomarn.Phase.type.Warp;
import xyz.nkomarn.Phase.util.Config;
import xyz.nkomarn.Phase.util.Search;
import xyz.nkomarn.Phase.util.WarpUtil;

public class PlayerWarpsHandler implements GUIHandler {
    @Override
    public void handle(Player player, int slot, InventoryClickEvent event) {
        GUIHolder holder = (GUIHolder) event.getInventory().getHolder();
        int page = holder.getPage();

        if (slot == 39) {
            if (page <= 1) {
                new MainMenu(player);
            } else {
                new PlayerWarps(player, Math.max(0, --page));
            }
        } else if (slot == 40) {
            player.closeInventory();
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    Config.getPrefix() + "&7Use the /setwarp command to create a warp."));
        } else if (slot == 41) {
            new PlayerWarps(player, Math.max(0, ++page));
        } else {
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() != Material.AIR
                    && clickedItem.getType()
                    != Material.WHITE_STAINED_GLASS_PANE) {
                String name = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
                if (name.trim().length() < 1) return;
                Search.getWarpByName(name).ifPresent(warp -> new OptionsMenu(player, warp));
            }
        }
    }
}
