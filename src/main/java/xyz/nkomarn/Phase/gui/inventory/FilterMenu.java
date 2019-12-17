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

import java.util.Arrays;

public class FilterMenu {
    public FilterMenu(final Player player) {
        Inventory menu = Bukkit.createInventory(new GUIHolder(GUIType.FILTER, 1), 27, "Filter by Category");
        player.openInventory(menu);

        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);
        Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26)
                .forEach(slot -> menu.setItem(slot, glass));

        // TODO

        player.openInventory(menu);
    }
}
