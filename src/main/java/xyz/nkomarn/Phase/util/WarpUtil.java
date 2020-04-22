package xyz.nkomarn.Phase.util;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import xyz.nkomarn.Kerosene.util.AdvancementUtil;
import xyz.nkomarn.Kerosene.util.LocationUtil;
import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.Phase.type.Warp;
import xyz.nkomarn.Kerosene.data.LocalStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;


public class WarpUtil {
    private static final DecimalFormat FORMATTER = new DecimalFormat("#,###");

    /**
     * Teleports a player asynchronously to a warp location.
     * @param player Player to teleport.
     * @param warp The warp object.
     */
    public static void warpPlayer(final Player player, final Warp warp) {
        final Location location = warp.getLocation();

        try {
            LocationUtil.teleportPlayer(player, location);
            player.sendTitle(ChatColor.translateAlternateColorCodes('&',
                    "&6&lWhoosh."), ChatColor.translateAlternateColorCodes('&',
                    String.format("You've arrived safely at '%s'.", warp.getName())));
            player.playSound(location, Sound.BLOCK_ENDER_CHEST_OPEN, 1.0f, 1.0f);
            player.getWorld().playEffect(warp.getLocation(), Effect.ENDER_SIGNAL, 15);
            if (!player.getUniqueId().equals(warp.getOwnerUUID())) Search.incrementVisits(warp);
        } catch (Exception e) {
            player.sendTitle(ChatColor.translateAlternateColorCodes('&',
                    "&c&lWarping failed."), ChatColor.translateAlternateColorCodes('&',
                    "A teleportation error occurred."));
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            e.printStackTrace();
        }

        final OfflinePlayer ownerOffline = Bukkit.getOfflinePlayer(warp.getOwnerUUID());
        if (ownerOffline.isOnline()) {
            final Player owner = (Player) ownerOffline;
            Bukkit.getScheduler().runTask(Phase.getPhase(), () -> {
                if (warp.getVisits() >= 1000) {
                    AdvancementUtil.grantAdvancement(owner, "warp-visits-1");
                    if (warp.getVisits() >= 10000) AdvancementUtil.grantAdvancement(owner, "warp-visits-2");
                }
            });
        }
    }

    /**
     * Adds a warp object into the database.
     * @param warp The warp object to insert.
     */
    public static void createWarp(final Warp warp) {
        final Location location = warp.getLocation();
        final String query = "INSERT INTO warps (name, owner, visits, category, featured, expired, renewed, x, y, z, " +
                "pitch, yaw, world) VALUES (?, ?, ?, ?, ?, ?, ?, ? ,?, ?, ?, ?, ?);";

        try (Connection connection = LocalStorage.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
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

                final OfflinePlayer ownerOffline = Bukkit.getOfflinePlayer(warp.getOwnerUUID());
                if (ownerOffline.isOnline()) {
                    final Player owner = (Player) ownerOffline;
                    Bukkit.getScheduler().runTask(Phase.getPhase(), () -> AdvancementUtil
                            .grantAdvancement(owner, "warp-create"));
                }

                Phase.getPhase().getLogger().info(String.format("%s created warp '%s'.",
                        ownerOffline.getName(), warp.getName()));
            }
        } catch (SQLException e) {
            e.printStackTrace();

            final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(warp.getOwnerUUID());
            if (offlinePlayer.isOnline()) {
                final Player player = (Player) offlinePlayer;
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cAn error " +
                                "occurred while creating your warp- notify an admin."));
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            }
        }
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
    }

    public static HashMap<String, Material> getCategories() {
        final HashMap<String, Material> categories = new HashMap<>();
        final ConfigurationSection section = Phase.getPhase().getConfig().getConfigurationSection("categories");
        section.getKeys(false).forEach(category -> categories.put(category, Material.valueOf(section.getString(category))));
        return categories;
    }

    public static Material getItem(final String category) {
        return getCategories().get(category);
    }

    public static boolean doesLocationHaveClaims(final Player player, final Location location) {
        final Collection<Claim> claims = GriefPrevention.instance.dataStore.getClaims(location.getChunk().getX(),
                location.getChunk().getZ());

        if (claims.size() > 0) {
            for (Claim claim : claims) {
                if (!claim.getOwnerName().equals(player.getName())) {
                    if (!claim.managers.contains(player.getUniqueId().toString())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String formatNumber(final int number) {
        return FORMATTER.format(number);
    }
}
