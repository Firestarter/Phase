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
import xyz.nkomarn.Phase.util.WarpUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class CategoryPicker {
    public CategoryPicker(final Player player, final Warp warp) {
        Inventory menu = Bukkit.createInventory(new GUIHolder(GUIType.CATEGORY_PICKER, 1, warp.getName()), 9, "Select Category");

        ItemStack glass = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);
        Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8).forEach(slot -> menu.setItem(slot, glass));

        final ArrayList<String> categories = new ArrayList<>(WarpUtil.getCategories().keySet());
        categories.forEach(category -> {
            ItemStack categoryItem = new ItemStack(WarpUtil.getItem(category), 1);
            ItemMeta categoryItemMeta = categoryItem.getItemMeta();
            categoryItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', String.format(
                    "&6&l%s", category
            )));
            categoryItem.setItemMeta(categoryItemMeta);
            menu.setItem(categories.indexOf(category), categoryItem);
        });
        player.openInventory(menu);
    }
}
