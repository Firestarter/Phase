package xyz.nkomarn.Phase.util;

import com.firestartermc.kerosene.util.AdvancementUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.Phase.type.Category;
import xyz.nkomarn.Phase.type.Warp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.UUID;

public class WarpUtil {
    private static final DecimalFormat FORMATTER = new DecimalFormat("#,###");

    /**
     * Teleports a player asynchronously to a warp location.
     *
     * @param player Player to teleport.
     * @param warp   The warp object.
     */
    @Deprecated
    public static void warpPlayer(Player player, Warp warp) {
        warp.teleport(player);
    }

    public static void createWarp(String name, UUID owner, Location location, Category category) {
        String query = "INSERT INTO warps (name, owner, visits, category, featured, expired, renewed, x, y, z, " +
                "pitch, yaw, world) VALUES (?, ?, ?, ?, ?, ?, ?, ? ,?, ?, ?, ?, ?);";

        try (Connection connection = Phase.getStorage().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, name);
                statement.setString(2, owner.toString());
                statement.setInt(3, 0);
                statement.setString(4, category.getName());
                statement.setBoolean(5, false);
                statement.setBoolean(6, false);
                statement.setLong(7, System.currentTimeMillis());
                statement.setDouble(8, location.getX());
                statement.setDouble(9, location.getY());
                statement.setDouble(10, location.getZ());
                statement.setDouble(11, location.getPitch());
                statement.setDouble(12, location.getYaw());
                statement.setString(13, location.getWorld().getUID().toString());
                statement.execute();

                var ownerOffline = Bukkit.getOfflinePlayer(owner);
                if (ownerOffline.isOnline()) {
                    AdvancementUtils.grant((Player) ownerOffline, "warp-create");
                }

                Phase.getPhase().getLogger().info(String.format("%s created warp '%s'.", ownerOffline.getName(), name));
            }
        } catch (SQLException e) {
            e.printStackTrace();

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(owner);
            if (offlinePlayer.isOnline()) {
                Player player = (Player) offlinePlayer;
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cAn error " +
                        "occurred while creating your warp- notify an admin."));
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            }
        }
    }

    public static void changeWarpCategory(String warpName, Category category) {
        try (Connection connection = Phase.getStorage().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE warps SET category = ? WHERE name LIKE ?;")) {
                statement.setString(1, category.getName());
                statement.setString(2, warpName);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void renameWarp(String warpName, String newName) {
        try (Connection connection = Phase.getStorage().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE warps SET name = ? WHERE name LIKE ?;")) {
                statement.setString(1, newName);
                statement.setString(2, warpName);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void relocateWarp(String warpName, Location newLocation) {
        try (Connection connection = Phase.getStorage().getConnection()) {
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

    public static void delete(String warpName) {
        try (Connection connection = Phase.getStorage().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM warps WHERE name LIKE ?;")) {
                statement.setString(1, warpName);
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String formatNumber(int number) {
        return FORMATTER.format(number);
    }

    public static String argsToString(String[] args) {
        StringBuilder builder = new StringBuilder();
        Arrays.stream(args).forEach(arg -> builder.append(arg).append(" "));
        return builder.toString().trim();
    }
}
