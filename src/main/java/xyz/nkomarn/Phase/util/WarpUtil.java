package xyz.nkomarn.Phase.util;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.Phase.type.Warp;

import java.util.ArrayList;
import java.util.HashMap;

public class WarpUtil {

    /**
     * Adds a warp object into the database
     * @param warp The warp object to insert
     */
    public static void createWarp(final Warp warp) {
        final Location location = warp.getLocation();
        Document document = new Document("name", warp.getName())
                .append("owner", warp.getOwnerUUID().toString())
                .append("visits", warp.getVisits())
                .append("type", warp.getType())
                .append("category", warp.getCategory())
                .append("featured", warp.isFeatured())
                .append("expired", warp.isExpired())
                .append("renewed", warp.getRenewedTime())
                .append("x", location.getX())
                .append("y", location.getY())
                .append("z", location.getZ())
                .append("pitch", location.getPitch())
                .append("yaw", location.getYaw())
                .append("world", location.getWorld().getUID().toString())
                .append("favorites", warp.getFavorites());
        Phase.getCollection().sync().insertOne(document); // TODO async with subscriber
    }

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
                        String.format("You've arrived safely at '%s'.", warp.getName())));
                player.playSound(location, Sound.BLOCK_ENDER_CHEST_OPEN, 1.0f, 1.0f);
                player.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, 15);
                Phase.getCollection().sync().updateOne(Filters.eq("name", warp.getName()), new Document("$inc",
                        new BasicDBObject().append("visits", 1)));
            } else {
                player.sendTitle(ChatColor.translateAlternateColorCodes('&',
                        "&c&lTeleporter machine broke."), ChatColor.translateAlternateColorCodes('&',
                        String.format("Some teleportation error occurred.")));
                player.playSound(location, Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            }
        });
    }

    /**
     * Adds a player to the favorites list of a warp
     */
    public static void favorite(final Player player, final Warp warp) {
        ArrayList<String> favorites = warp.getFavorites();
        favorites.add(player.getUniqueId().toString());
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
