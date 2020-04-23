package xyz.nkomarn.Phase.util;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.*;
import org.bukkit.entity.Player;
import xyz.nkomarn.Kerosene.util.AdvancementUtil;
import xyz.nkomarn.Kerosene.util.LocationUtil;
import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.Phase.type.Category;
import xyz.nkomarn.Phase.type.Warp;
import xyz.nkomarn.Kerosene.data.LocalStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Collection;

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
    public static void createWarp(final Warp warp) { // TODO direct args, dont require warp object
        final Location location = warp.getLocation();
        final String query = "INSERT INTO warps (name, owner, visits, category, featured, expired, renewed, x, y, z, " +
                "pitch, yaw, world) VALUES (?, ?, ?, ?, ?, ?, ?, ? ,?, ?, ?, ?, ?);";

        try (Connection connection = LocalStorage.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, warp.getName());
                statement.setString(2, warp.getOwnerUUID().toString());
                statement.setInt(3, warp.getVisits());
                statement.setString(4, warp.getCategory().getName());
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

    public static void changeWarpCategory(final String warpName, final Category category) {
        try (Connection connection = LocalStorage.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE warps SET category = ? WHERE name LIKE ?;")) {
                statement.setString(1, category.getName());
                statement.setString(2, warpName);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void renameWarp(final String warpName, final String newName) {
        try (Connection connection = LocalStorage.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE warps SET name = ? WHERE name LIKE ?;")) {
                statement.setString(1, newName);
                statement.setString(2, warpName);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void relocateWarp(final String warpName, final Location newLocation) {
        try (Connection connection = LocalStorage.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE warps SET x = ?, y = ?, z = ?, " +
                    "pitch = ?, yaw = ?, world = ? WHERE name LIKE ?;")) {
                statement.setDouble(1, newLocation.getX());
                statement.setDouble(2, newLocation.getY());
                statement.setDouble(3, newLocation.getZ());
                statement.setDouble(4, newLocation.getPitch());
                statement.setDouble(5, newLocation.getYaw());
                statement.setString(6, newLocation.getWorld().getUID().toString());
                statement.setString(7, warpName);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(final String warpName) {
        try (Connection connection = LocalStorage.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM warps WHERE name LIKE ?;")) {
                statement.setString(1, warpName);
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
