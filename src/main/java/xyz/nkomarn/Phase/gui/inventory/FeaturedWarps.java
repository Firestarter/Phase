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

public class FeaturedWarps {
    public FeaturedWarps(Player player, int page) {
        Inventory menu = Bukkit.createInventory(new GuiHolder(GuiType.FEATURED_WARPS, page), 45,
                String.format("Featured Warps (Page %s)", page));

        ItemStack glass = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);
        Arrays.asList(36, 37, 38, 40, 42, 43, 44).forEach(slot -> menu.setItem(slot, glass));

        ItemStack previous = new ItemStack(Material.SPRUCE_BUTTON, 1);
        ItemMeta previousMeta = previous.getItemMeta();
        previousMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b&lPrevious"));
        previous.setItemMeta(previousMeta);
        menu.setItem(39, previous);

        ItemStack next = new ItemStack(Material.SPRUCE_BUTTON, 1);
        ItemMeta nextMeta = next.getItemMeta();
        nextMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b&lNext"));
        next.setItemMeta(nextMeta);
        menu.setItem(41, next);

        Bukkit.getScheduler().runTask(Phase.getPhase(), () -> {
            List<Warp> warps = Search.getFeaturedWarps();
            int start = Math.min(Math.max(36 * (page - 1), 0), warps.size());
            int end = Math.min(Math.max(36 * page, start), warps.size());
            warps.subList(start, end).forEach(warp -> menu.setItem(warps.indexOf(warp) % 36, warp.getItemStack()));
            player.openInventory(menu);
        });
    }
}
