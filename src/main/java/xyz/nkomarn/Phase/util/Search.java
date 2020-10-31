package xyz.nkomarn.Phase.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.Phase.type.Category;
import xyz.nkomarn.Phase.type.Warp;;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class Search {
    /**
     * Returns every single currently created public warp.
     * @return ArrayList of every warp object in the database.
     */
    public static List<Warp> getPublicWarps() {
        try (Connection connection = Phase.getStorage().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM warps WHERE expired = 0 " +
                    "ORDER BY visits DESC;")) {
                return resultToSet(statement.executeQuery());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Returns 36 warp objects that would go on a specified public warps page.
     * @param page The public warps list page to fetch warps for.
     * @return A set of public warps that should go on the page.
     */
    public static List<Warp> getPublicWarpsPage(int page) {
        try (Connection connection = Phase.getStorage().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM (SELECT * FROM warps " +
                    "WHERE expired = 0 ORDER BY visits DESC) LIMIT 36 OFFSET ?;")) {
                statement.setFetchSize(36);
                statement.setInt(1, Math.max(36 * (page - 1), 0));
                return resultToSet(statement.executeQuery());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Returns all of the warps a player owns.
     * @param uuid Player's UUID.
     * @return ArrayList of player's warps.
     */
    public static List<Warp> getPlayerWarps(UUID uuid) {
        try (Connection connection = Phase.getStorage().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM warps WHERE owner = ? " +
                    "ORDER BY visits DESC;")) {
                statement.setString(1, uuid.toString());
                return resultToSet(statement.executeQuery());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Returns a list of all of the public featured warps
     * @return ArrayList of all public featured warps
     */
    public static List<Warp> getFeaturedWarps() {
        return getPublicWarps().stream()
                .filter(Warp::isFeatured)
                .collect(Collectors.toList());
    }

    /**
     * Returns a public warp by name.
     * @param name Warp name (case insensitive).
     * @return Warp object or null in the case of a warp not being found.
     */
    public static Optional<Warp> getWarpByName(String name) {
        try (Connection connection = Phase.getStorage().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM warps WHERE name LIKE ?;")) {
                statement.setString(1, name);
                try (ResultSet results = statement.executeQuery()) {
                    List<Warp> warpResults = resultToSet(results);
                    if (warpResults.size() > 0) {
                        return Optional.of(warpResults.get(0));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Returns all of the warps that match a category
     * @param category Selected category to search for
     * @return ArrayList of warps that match the category
     */
    public static List<Warp> getWarpsByCategory(String category) {
        try (Connection connection = Phase.getStorage().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM warps WHERE category = ? " +
                    "ORDER BY visits DESC;")) {
                statement.setString(1, category);
                return resultToSet(statement.executeQuery());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Increments the visit count for a warp
     * @param warp Warp object to increment
     */
    public static void incrementVisits(Warp warp) { // TODO IS RAN SYNC, THUS INNER MUST BE ASYNC
        Bukkit.getScheduler().runTaskAsynchronously(Phase.getPhase(), () -> {
            try (Connection connection = Phase.getStorage().getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("UPDATE warps SET visits = visits + 1 " +
                        "WHERE name LIKE ?;")) {
                    statement.setString(1, warp.getName());
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Turns a ResultSet into an ArrayList of warp objects.
     * @param results Results from a database query.
     * @return ArrayList of warp objects.
     */
    private static List<Warp> resultToSet(ResultSet results) throws SQLException {
        List<Warp> warps = new ArrayList<>();

        while (results.next()) {
            Location location = new Location(
                    Bukkit.getWorld(UUID.fromString(results.getString(14))),
                    results.getDouble(9),
                    results.getDouble(10),
                    results.getDouble(11),
                    results.getFloat(13),
                    results.getFloat(12)
            );

            warps.add(new Warp(
                    results.getString(2),
                    UUID.fromString(results.getString(3)),
                    location,
                    Category.valueOf(results.getString(5).toUpperCase()),
                    results.getInt(4),
                    results.getBoolean(6),
                    results.getBoolean(7),
                    results.getLong(8)
            ));
        }

        return warps;
    }
}
