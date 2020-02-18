package xyz.nkomarn.Phase.util;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import xyz.nkomarn.Kerosene.paperlib.PaperLib;
import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.Phase.type.Warp;

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
        Search.cacheWarp(warp);

        Player player = Bukkit.getPlayer(warp.getOwnerUUID());
        if (player == null) return;
        if (!Advancements.isComplete(player, "warp-create")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("advancement grant %s only firestarter:warp-create",
                    player.getName()));
        }
    }

    /**
     * Teleports a player asynchronously to a warp location
     * @param player Player to teleport
     * @param warp The warp object
     */
    public static void warp(final Player player, final Warp warp) { // TODO warp safety checking
        final Location location = warp.getLocation();
        // TODO warp safety check
        PaperLib.teleportAsync(player, location.add(0 , 0.2, 0)).thenAccept(result -> {
            if (result) {
                player.sendTitle(ChatColor.translateAlternateColorCodes('&',
                        "&6&lWhoosh."), ChatColor.translateAlternateColorCodes('&',
                        String.format("You've arrived safely at '%s'.", warp.getName())));
                player.playSound(location, Sound.BLOCK_ENDER_CHEST_OPEN, 1.0f, 1.0f);
                player.getWorld().playEffect(player.getLocation(), Effect.ENDER_SIGNAL, 15);
                Search.incrementVisits(warp);
            } else {
                player.sendTitle(ChatColor.translateAlternateColorCodes('&',
                        "&c&lTeleporter machine broke."), ChatColor.translateAlternateColorCodes('&',
                        "Some teleportation error occurred."));
                player.playSound(location, Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            }
        });

        Player owner = Bukkit.getPlayer(warp.getOwnerUUID());
        if (owner == null) return;

        if (warp.getVisits().get() >= 1000) {
            if (!Advancements.isComplete(player, "warp-visits-1")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("advancement grant %s only firestarter:warp-visits-1",
                        owner.getName()));
            }

            if (warp.getVisits().get() >= 10000) {
                if (!Advancements.isComplete(player, "warp-visits-2")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("advancement grant %s only firestarter:warp-visits-2",
                            owner.getName()));
                }
            }
        }
    }

    public static void rename(final Warp warp, final String name) {
        Phase.getCollection().sync().updateOne(Filters.eq("name", warp.getName()), new Document("$set",
                new BasicDBObject().append("name", name)));
        Search.remove(warp);
        warp.setName(name);
        Search.cacheWarp(warp);
    }

    public static void relocate(final Player player, final Warp warp) {
        final Location location = player.getLocation();
        warp.setLocation(location); // TODO warp safety checking
        Phase.getCollection().sync().updateOne(Filters.eq("name", warp.getName()), new Document("$set",
                new BasicDBObject().append("x", location.getX()).append("y", location.getY())
                        .append("z", location.getZ()).append("pitch", location.getPitch())
                        .append("yaw", location.getYaw()).append("world", location.getWorld().getUID().toString()))); // TODO async w/ subscriber
        player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6&lRelocated"),
                ChatColor.translateAlternateColorCodes('&', "The warp is now at your location."));
        player.playSound(location, Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, 1.0f);
    }

    public static void delete(final Player player, final Warp warp) {
        Phase.getCollection().sync().deleteOne(Filters.eq("name", warp.getName())); // A S Y N C  PLZ
        Search.remove(warp);
        player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&c&lDeleted"),
                ChatColor.translateAlternateColorCodes('&', "The warp was blown away with the wind."));
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
    }

    /**
     * Adds a player to the favorites list of a warp
     */
    public static void favorite(final Player player, final Warp warp) {
        warp.addFavorite(player);
        Phase.getCollection().sync().updateOne(Filters.eq("name", warp.getName()), new Document("$set",
                new BasicDBObject().append("favorites", warp.getFavorites()))); // TODO async w/ subscriber
    }

    /**
     * Removes a player from the favorites list of a warp
     */
    public static void unFavorite(final Player player, final Warp warp) {
        warp.removeFavorite(player);
        Phase.getCollection().sync().updateOne(Filters.eq("name", warp.getName()), new Document("$set",
                new BasicDBObject().append("favorites", warp.getFavorites()))); // TODO async w/ subscriber
    }

    private static HashMap<String, Material> getCategories() {
        HashMap<String, Material> categories = new HashMap<>();
        ConfigurationSection section = Phase.getPhase().getConfig().getConfigurationSection("categories");
        for (String category : section.getKeys(false)) {
            categories.put(category, Material.valueOf(section.getString(category)));
        }
        return categories;
    }

    public static Material getItem(final String category) {
        return getCategories().get(category);
    }
}
