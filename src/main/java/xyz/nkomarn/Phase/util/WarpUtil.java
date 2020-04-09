package xyz.nkomarn.Phase.util;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import xyz.nkomarn.Kerosene.util.LocationUtil;
import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.Phase.type.Warp;
import xyz.nkomarn.Kerosene.data.LocalStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;


public class WarpUtil {
    /**
     * Teleports a player asynchronously to a warp location
     * @param player Player to teleport
     * @param warp The warp object
     */
    public static void warp(final Player player, final Warp warp) {
        final Location location = warp.getLocation();

        try {
            LocationUtil.teleportPlayer(player, location);
            player.sendTitle(ChatColor.translateAlternateColorCodes('&',
                    "&6&lWhoosh."), ChatColor.translateAlternateColorCodes('&',
                    String.format("You've arrived safely at '%s'.", warp.getName())));
            player.playSound(location, Sound.BLOCK_ENDER_CHEST_OPEN, 1.0f, 1.0f);
            player.getWorld().playEffect(player.getLocation(), Effect.ENDER_SIGNAL, 15);
            Search.incrementVisits(warp);
        } catch (Exception e) {
            player.sendTitle(ChatColor.translateAlternateColorCodes('&',
                    "&c&lTeleporter machine broke."), ChatColor.translateAlternateColorCodes('&',
                    "Some teleportation error occurred."));
            player.playSound(location, Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
        }

        //Player owner = Bukkit.getPlayer(warp.getOwnerUUID());
        //if (owner == null) throw new NullPointerException("Invalid player object during teleportation.");;

        /*if (warp.getVisits() >= 1000) {
            if (!Advancements.isComplete(player, "warp-visits-1")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("advancement grant %s only firestarter:warp-visits-1",
                        owner.getName()));
            }

            if (warp.getVisits() >= 10000) {
                if (!Advancements.isComplete(player, "warp-visits-2")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("advancement grant %s only firestarter:warp-visits-2",
                            owner.getName()));
                }
            }
        }*/
    }

    /**
     * Adds a warp object into the database
     * @param warp The warp object to insert
     */
    public static void createWarp(final Warp warp) throws SQLException {
        final Location location = warp.getLocation();
        Connection connection = LocalStorage.getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO warps (name, owner, visits, category, " +
                "featured, expired, renewed, x, y, z, pitch, yaw, world) VALUES (?, ?, ?, ?, ?, ?, ?, ? ,?, ?, ?, " +
                "?, ?);");
        statement.setString(1, warp.getName());
        statement.setString(2, warp.getOwnerUUID().toString());
        statement.setInt(3, warp.getVisits());
        statement.setString(4, warp.getCategory());
        statement.setBoolean(5, warp.isFeatured());
        statement.setBoolean(6, warp.isExpired());
        statement.setLong(7, warp.getRenewedTime());
        statement.setDouble(8, location.getX());
        statement.setDouble(9, location.getY());
        statement.setDouble(10, location.getZ());
        statement.setDouble(11, location.getPitch());
        statement.setDouble(12, location.getYaw());
        statement.setString(13, location.getWorld().getUID().toString());
        statement.execute();

        Player player = Bukkit.getPlayer(warp.getOwnerUUID());
        if (player == null) throw new NullPointerException("Invalid player object when creating warp.");
        //if (!Advancements.isComplete(player, "warp-create")) {
            /*Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("advancement grant %s only firestarter:warp-create",
                    player.getName()));*/
        //}
    }

    public static void changeCategory(final Warp warp, final String category) {
        Connection connection = null;

        try {
            connection = LocalStorage.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE warps SET category = ? WHERE " +
                    "name LIKE ?;");
            statement.setString(1, category);
            statement.setString(2, warp.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void rename(final Warp warp, final String name) {
        Connection connection = null;

        try {
            connection = LocalStorage.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE warps SET name = ? WHERE " +
                    "name LIKE ?;");
            statement.setString(1, name);
            statement.setString(2, warp.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void relocate(final Player player, final Warp warp) {
        final Location location = player.getLocation();
        Connection connection = null;

        try {
            connection = LocalStorage.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE warps SET x = ?, y = ?, z = ?," +
                    "pitch = ?, yaw = ?, world = ? WHERE name LIKE ?;");
            statement.setDouble(1, location.getX());
            statement.setDouble(2, location.getY());
            statement.setDouble(3, location.getZ());
            statement.setDouble(4, location.getPitch());
            statement.setDouble(5, location.getYaw());
            statement.setString(6, location.getWorld().getUID().toString());
            statement.setString(7, warp.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6&lRelocated"),
                ChatColor.translateAlternateColorCodes('&', "The warp is now at your location."));
        player.playSound(location, Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, 1.0f);
    }

    public static void delete(final Player player, final Warp warp) {
        Connection connection = null;

        try {
            connection = LocalStorage.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM warps WHERE name LIKE ?;");
            statement.setString(1, warp.getName());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&c&lDeleted"),
                ChatColor.translateAlternateColorCodes('&', "The warp was blown away with the wind."));
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
    }

    public static HashMap<String, Material> getCategories() {
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
