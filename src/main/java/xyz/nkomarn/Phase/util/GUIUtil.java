package xyz.nkomarn.Phase.util;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.nkomarn.Phase.type.Warp;

import java.util.Arrays;

public class GUIUtil {

    /**
     * Returns the correct ItemStack for the warp type
     * @param warp Warp object to return ItemStack for
     * @return Correct ItemStack for warp object
     */
    public static ItemStack getWarpItem(final Warp warp) {
        ItemStack warpItem = new ItemStack(WarpUtil.getItem(warp.getCategory()));
        ItemMeta warpItemMeta = warpItem.getItemMeta();
        warpItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                String.format("&6&l%s", warp.getName())));
        warpItemMeta.setLore(Arrays.asList(
                ChatColor.translateAlternateColorCodes('&', String.format("&7Category: &6%s", warp.getCategory())),
                ChatColor.translateAlternateColorCodes('&', String.format("&7Visits: &6%s", warp.getVisits()))
        ));
        warpItem.setItemMeta(warpItemMeta);
        return warpItem;
    }
}
