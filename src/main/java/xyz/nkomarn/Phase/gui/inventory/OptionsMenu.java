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

import java.util.Arrays;

public class OptionsMenu {
    public OptionsMenu(Player player, Warp warp) {
        Inventory menu = Bukkit.createInventory(new GUIHolder(GUIType.OPTIONS, 1, warp.getName()), 9,
                String.format("Options: '%s'", warp.getName()));
        player.openInventory(menu);

        ItemStack glass = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);
        Arrays.asList(0, 1, 7, 8)
                .forEach(slot -> menu.setItem(slot, glass));

        ItemStack teleport = new ItemStack(Material.ENDER_EYE, 1);
        ItemMeta teleportMeta = teleport.getItemMeta();
        teleportMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&d&lTeleport"));
        teleportMeta.setLore(Arrays.asList(ChatColor.GRAY + "Teleport to your", ChatColor.GRAY + "warp's location."));
        teleport.setItemMeta(teleportMeta);
        menu.setItem(2, teleport);

        ItemStack rename = new ItemStack(Material.NAME_TAG, 1);
        ItemMeta renameMeta = rename.getItemMeta();
        renameMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lRename"));
        renameMeta.setLore(Arrays.asList(ChatColor.GRAY + "Change your", ChatColor.GRAY + "warp's name."));
        rename.setItemMeta(renameMeta);
        menu.setItem(3, rename);

        ItemStack relocate = new ItemStack(Material.FILLED_MAP, 1);
        ItemMeta relocateMeta = relocate.getItemMeta();
        relocateMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lRelocate"));
        relocateMeta.setLore(Arrays.asList(ChatColor.GRAY + "Move your warp to", ChatColor.GRAY + "your current location."));
        relocate.setItemMeta(relocateMeta);
        menu.setItem(4, relocate);

        ItemStack category = new ItemStack(Material.HOPPER, 1);
        ItemMeta categoryMeta = category.getItemMeta();
        categoryMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lChange Category"));
        categoryMeta.setLore(Arrays.asList(ChatColor.GRAY + "Change your warp's", ChatColor.GRAY + "category."));
        category.setItemMeta(categoryMeta);
        menu.setItem(5, category);

        ItemStack delete = new ItemStack(Material.BARRIER, 1);
        ItemMeta deleteMeta = delete.getItemMeta();
        deleteMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lDelete"));
        deleteMeta.setLore(Arrays.asList(ChatColor.GRAY + "Delete your warp", ChatColor.GRAY + "(irreversible)."));
        delete.setItemMeta(deleteMeta);
        menu.setItem(6, delete);

        player.openInventory(menu);
    }
}
