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
import xyz.nkomarn.Phase.type.Warp;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class CategoryPicker {
    public CategoryPicker(Player player, Warp warp) {
        Inventory menu = Bukkit.createInventory(new GuiHolder(GuiType.CATEGORY_PICKER, 1, warp.getName()), 9, "Select Category");

        ItemStack glass = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);
        Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8).forEach(slot -> menu.setItem(slot, glass));

        AtomicInteger slot = new AtomicInteger();
        Arrays.stream(Category.values()).forEach(category -> {
            ItemStack categoryItem = new ItemStack(category.getMaterial());
            ItemMeta categoryItemMeta = categoryItem.getItemMeta();
            categoryItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', String.format(
                    "&f&l%s", category.getName()
            )));
            categoryItem.setItemMeta(categoryItemMeta);
            categoryItem.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
            menu.setItem(slot.incrementAndGet() - 1, categoryItem);
        });
        player.openInventory(menu);
    }
}
