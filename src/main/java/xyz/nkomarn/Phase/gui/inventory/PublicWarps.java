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
import xyz.nkomarn.Phase.util.Search;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class PublicWarps {
    public PublicWarps(Player player, int page) {
        Inventory menu = Bukkit.createInventory(new GuiHolder(GuiType.PUBLIC_WARPS, page), 45,
                String.format("Public Warps (Page %s)", page));

        ItemStack glass = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);
        Arrays.asList(36, 37, 38, 42, 43, 44).forEach(slot -> menu.setItem(slot, glass));

        ItemStack previous = new ItemStack(Material.SPRUCE_BUTTON, 1);
        ItemMeta previousMeta = previous.getItemMeta();
        previousMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b&lPrevious"));
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
        nextMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b&lNext"));
        next.setItemMeta(nextMeta);
        menu.setItem(41, next);

        Bukkit.getScheduler().runTaskAsynchronously(Phase.getPhase(), () -> {
            AtomicInteger slot = new AtomicInteger();
            Search.getPublicWarpsPage(page).forEach(warp -> menu.setItem(slot.getAndIncrement(), warp.getItemStack()));
            Bukkit.getScheduler().runTask(Phase.getPhase(), () -> player.openInventory(menu));
        });
    }
}
