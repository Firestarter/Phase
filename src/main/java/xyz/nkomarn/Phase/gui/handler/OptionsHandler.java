package xyz.nkomarn.Phase.gui.handler;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import xyz.nkomarn.Phase.gui.GUIHolder;
import xyz.nkomarn.Phase.gui.inventory.FeaturedWarps;
import xyz.nkomarn.Phase.gui.inventory.PlayerWarps;
import xyz.nkomarn.Phase.gui.inventory.PublicWarps;
import xyz.nkomarn.Phase.type.Warp;
import xyz.nkomarn.Phase.util.Config;
import xyz.nkomarn.Phase.util.Search;
import xyz.nkomarn.Phase.util.WarpUtil;

public class OptionsHandler implements GUIHandler {
    @Override
    public void handle(Player player, int slot, InventoryClickEvent event) {
        final GUIHolder holder = (GUIHolder) event.getInventory().getHolder();

        player.closeInventory();
        final Warp warp = Search.getWarpByName(holder.getData().toLowerCase());
        if (warp == null) return;

        if (slot == 2) {
            WarpUtil.warp(player, warp);
        } else if (slot == 3) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                    "%sThis feature is currently not implemented. Check back in a few days.", Config.getPrefix()
            )));
        } else if (slot == 4) {
            WarpUtil.relocate(player, warp);
        } else if (slot == 5) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                    "%sThis feature is currently not implemented. Check back in a few days.", Config.getPrefix()
            )));
        } else if (slot == 6) {
            WarpUtil.delete(player, warp);
        }
    }
}
