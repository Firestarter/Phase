package xyz.nkomarn.Phase.util;

import org.bukkit.Bukkit;
import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.Phase.type.Warp;
import xyz.nkomarn.Kerosene.data.LocalStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

public class Search {
    /**
     * Increments the visit count for a warp
     * @param warp Warp object to increment
     */
    public static void incrementVisits(final Warp warp) {
        Connection connection = null;

        try {
            connection = LocalStorage.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE warps SET visits = visits + 1 " +
                    "WHERE name LIKE ?;");
            statement.setString(1, warp.getName());
            statement.executeUpdate();
        }catch (SQLException e) {
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

    /**
     * Returns a public warp by name
     * @param name Warp name (case insensitive)
     * @return Warp object or null in the case of a warp not being found
     */ // TODO figure out private warp handling with this
    public static Warp getWarpByName(final String name) {
        Connection connection = null;

        try {
            connection = LocalStorage.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM warps WHERE name LIKE ?;");
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                return new Warp(result.getString(2), result.getString(3), result.getInt(4),
                        result.getString(5), result.getBoolean(6), result.getBoolean(7),
                        result.getLong(8), result.getDouble(9), result.getDouble(10),
                        result.getDouble(11), result.getDouble(12), result.getDouble(13),
                        result.getString(14));
            }
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
        return null;
    }

    /**
     * Returns every single currently created public warp
     * @return ArrayList of every warp object in the database
     */
    public static ArrayList<Warp> getPublicWarps() {
        ArrayList<Warp> warps = new ArrayList<>();
        Connection connection = null;

        try {
            connection = LocalStorage.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM warps WHERE expired = 0 ORDER BY visits DESC;");
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                warps.add(new Warp(result.getString(2), result.getString(3), result.getInt(4),
                        result.getString(5), result.getBoolean(6), result.getBoolean(7),
                        result.getLong(8), result.getDouble(9), result.getDouble(10),
                        result.getDouble(11), result.getDouble(12), result.getDouble(13),
                        result.getString(14)));
            }
            return warps;
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
        return warps;
    }

    /**
     * Returns a lost of all of the public featured warps
     * @return ArrayList of all public featured warps
     */
    public static ArrayList<Warp> getFeaturedWarps() {
        return (ArrayList<Warp>) getPublicWarps().stream()
                .filter(Warp::isFeatured)
                .collect(Collectors.toList());
    }

    /**
     * Returns all of the warps a player owns
     * @param uuid Player's UUID
     * @return ArrayList of player's warps
     */
    public static ArrayList<Warp> getPlayerWarps(final UUID uuid) {
        ArrayList<Warp> warps = new ArrayList<>();
        Connection connection = null;

        try {
            connection = LocalStorage.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM warps WHERE owner = ? ORDER BY visits DESC;");
            statement.setString(1, uuid.toString());
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                warps.add(new Warp(result.getString(2), result.getString(3), result.getInt(4),
                        result.getString(5), result.getBoolean(6), result.getBoolean(7),
                        result.getLong(8), result.getDouble(9), result.getDouble(10),
                        result.getDouble(11), result.getDouble(12), result.getDouble(13),
                        result.getString(14)));
            }
            return warps;
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
        return warps;
    }

    /**
     * Returns all of the warps that match a category
     * @param category Selected category to search for
     * @return ArrayList of warps that match the category
     */
    public static ArrayList<Warp> getWarpsByCategory(final String category) {
        ArrayList<Warp> warps = new ArrayList<>();
        Connection connection = null;

        try {
            connection = LocalStorage.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM warps WHERE category = ? ORDER BY visits DESC;");
            statement.setString(1, category);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                warps.add(new Warp(result.getString(2), result.getString(3), result.getInt(4),
                        result.getString(5), result.getBoolean(6), result.getBoolean(7),
                        result.getLong(8), result.getDouble(9), result.getDouble(10),
                        result.getDouble(11), result.getDouble(12), result.getDouble(13),
                        result.getString(14)));
            }
            return warps;
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
        return warps;
    }
}
