package xyz.nkomarn.Phase.util;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.Phase.type.Warp;

import java.util.HashMap;

public class WarpUtil {

    /**
     * Teleports a player asynchronously to a warp location
     * @param player Player to teleport
     * @param warp The warp object
     */
    public static void warp(final Player player, final Warp warp) { // TODO warp safety checking
        final Location location = warp.getLocation();
        player.teleportAsync(location).thenAccept(result -> {
            if (result) {
                player.sendTitle(ChatColor.translateAlternateColorCodes('&',
                        "&6&lWhoosh."), ChatColor.translateAlternateColorCodes('&',
                        String.format("You've arrived safely at %s.", warp.getName())));
                player.playSound(location, Sound.BLOCK_ENDER_CHEST_OPEN, 1.0f, 1.0f);
            } else {
                player.sendTitle(ChatColor.translateAlternateColorCodes('&',
                        "&c&lTeleporter machine broke."), ChatColor.translateAlternateColorCodes('&',
                        String.format("Some teleportation error occurred.")));
                player.playSound(location, Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            }
        });
    }

    private static HashMap<String, Material> getCategories() {
        HashMap<String, Material> categories = new HashMap<>();
        ConfigurationSection section = Phase.getInstance().getConfig().getConfigurationSection("categories");
        for (String category : section.getKeys(false)) {
            categories.put(category, Material.valueOf(section.getString(category)));
        }
        return categories;
    }

    public static Material getItem(final String category) {
        return getCategories().get(category);
    }
}
