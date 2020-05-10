package xyz.nkomarn.Phase.gui.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.Phase.gui.GuiHolder;
import xyz.nkomarn.Phase.gui.GuiType;
import xyz.nkomarn.Phase.type.Warp;
import xyz.nkomarn.Phase.util.Search;

import java.util.Arrays;
import java.util.List;

public class FilteredWarps {
    public FilteredWarps(Player player, int page, String category) {
        Inventory menu = Bukkit.createInventory(new GuiHolder(GuiType.FILTERED_WARPS, page, category), 45,
                String.format("%s Warps (Page %s)", category, page));

        ItemStack glass = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);
        Arrays.asList(36, 37, 38, 42, 43, 44).forEach(slot -> menu.setItem(slot, glass));

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

        Bukkit.getScheduler().runTaskAsynchronously(Phase.getPhase(), () -> {
            List<Warp> warps = Search.getWarpsByCategory(category);
            int start = Math.min(Math.max(36 * (page - 1), 0), warps.size());
            int end = Math.min(Math.max(36 * page, start), warps.size());
            warps.subList(start, end).forEach(warp -> menu.setItem(warps.indexOf(warp) % 36, warp.getItemStack()));
            Bukkit.getScheduler().runTask(Phase.getPhase(), () -> player.openInventory(menu));
        });
    }
}
