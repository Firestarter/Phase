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

public class MainMenu {
    public MainMenu(final Player player) {
        Inventory menu = Bukkit.createInventory(new GUIHolder(GUIType.MAINMENU, 1), 27, "Warps Menu");
        player.openInventory(menu);

        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);
        Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26)
                .forEach(slot -> menu.setItem(slot, glass));

        ItemStack information = new ItemStack(Material.WRITABLE_BOOK, 1);
        ItemMeta informationMeta = information.getItemMeta();
        informationMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b&lInformation"));
        informationMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click to learn how to", ChatColor.GRAY + "use the warps system!"));
        information.setItemMeta(informationMeta);
        menu.setItem(10, information);

        ItemStack featured = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta featuredMeta = featured.getItemMeta();
        featuredMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&d&lFeatured Warps"));
        featuredMeta.setLore(Arrays.asList(ChatColor.GRAY + "View our hand-picked", ChatColor.GRAY + "featured warps!"));
        featured.setItemMeta(featuredMeta);
        menu.setItem(12, featured);

        ItemStack publicWarps = new ItemStack(Material.BOOK, 1);
        ItemMeta publicWarpsMeta = publicWarps.getItemMeta();
        publicWarpsMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lPlayer Warps"));
        publicWarpsMeta.setLore(Arrays.asList(ChatColor.GRAY + "View the playerbase's", ChatColor.GRAY + "public warps!"));
        publicWarps.setItemMeta(publicWarpsMeta);
        menu.setItem(13, publicWarps);

        ItemStack privateWarps = new ItemStack(Material.CHEST, 1);
        ItemMeta privateWarpsMeta = privateWarps.getItemMeta();
        privateWarpsMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lYour Warps"));
        privateWarpsMeta.setLore(Arrays.asList(ChatColor.GRAY + "View and manage all", ChatColor.GRAY + "of your warps!"));
        privateWarps.setItemMeta(privateWarpsMeta);
        menu.setItem(14, privateWarps);

        player.openInventory(menu);
    }
}
