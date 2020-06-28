package xyz.nkomarn.Phase.gui.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.nkomarn.Phase.gui.GuiHolder;
import xyz.nkomarn.Phase.gui.GuiType;
import xyz.nkomarn.Phase.type.Category;

import java.util.Arrays;

public class FilterMenu {
    public FilterMenu(Player player) {
        Inventory menu = Bukkit.createInventory(new GuiHolder(GuiType.FILTER, 1), 9, "Filter by Category");

        ItemStack glass = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);
        Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8).forEach(slot -> menu.setItem(slot, glass));

        int i = 0;
        for (Category category : Category.values()) {
            ItemStack categoryItem = new ItemStack(category.getMaterial());
            ItemMeta categoryItemMeta = categoryItem.getItemMeta();
            categoryItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', String.format(
                    "&f&l%s", category.getName()
            )));
            categoryItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
            categoryItem.setItemMeta(categoryItemMeta);
            menu.setItem(i++, categoryItem);
        }
        player.openInventory(menu);
    }
}
