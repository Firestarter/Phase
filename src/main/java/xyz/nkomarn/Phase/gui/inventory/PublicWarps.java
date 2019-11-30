package xyz.nkomarn.Phase.gui.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.nkomarn.Phase.gui.GUIHolder;
import xyz.nkomarn.Phase.gui.GUIType;
import xyz.nkomarn.Phase.type.Warp;
import xyz.nkomarn.Phase.util.Search;
import xyz.nkomarn.Phase.util.WarpUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class PublicWarps {
    public PublicWarps(final Player player, final int page) {
        Inventory menu = Bukkit.createInventory(new GUIHolder(GUIType.PUBLICWARPS, page), 45,
                String.format("Public Warps (Page %s)", page));

        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);
        Arrays.asList(36, 37, 38, 40, 42, 43, 44).forEach(slot -> menu.setItem(slot, glass));

        ItemStack previous = new ItemStack(Material.SPRUCE_BUTTON, 1);
        ItemMeta previousMeta = previous.getItemMeta();
        previousMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lPrevious"));
        previous.setItemMeta(previousMeta);
        menu.setItem(39, previous);

        ItemStack filter = new ItemStack(Material.HOPPER, 1);
        ItemMeta filterMeta = filter.getItemMeta();
        filterMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lFilter by Category"));
        filterMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Display only warps of", ChatColor.GRAY + "a certain category."
        ));
        filter.setItemMeta(filterMeta);
        menu.setItem(40, filter);

        ItemStack next = new ItemStack(Material.SPRUCE_BUTTON, 1);
        ItemMeta nextMeta = next.getItemMeta();
        nextMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lNext"));
        next.setItemMeta(nextMeta);
        menu.setItem(41, next);

        try {
            ArrayList<Warp> warps = Search.getPublicWarps().get();
            final int totalWarps = warps.size();
            final int startingIndex = Math.min(Math.max(36 * (page - 1), 0), totalWarps);
            final int endingIndex = Math.min(Math.max(36 * page, startingIndex), warps.size());

            warps.subList(startingIndex, endingIndex).forEach(warp -> {
                ItemStack warpItem = new ItemStack(WarpUtil.getItem(warp.getCategory()));
                ItemMeta warpItemMeta = warpItem.getItemMeta();
                warpItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                        String.format("&7%s", warp.getName())));
                warpItemMeta.setLore(Arrays.asList(
                        ChatColor.translateAlternateColorCodes('&', String.format("&7Category: &6%s", warp.getCategory())),
                        ChatColor.translateAlternateColorCodes('&', String.format("&7Visits: &6%s", warp.getVisits()))
                ));
                warpItem.setItemMeta(warpItemMeta);
                menu.setItem(warps.indexOf(warp) % 36, warpItem);
            });
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        player.openInventory(menu);
    }
}
