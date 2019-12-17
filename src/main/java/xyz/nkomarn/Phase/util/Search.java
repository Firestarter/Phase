package xyz.nkomarn.Phase.util;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.Phase.type.Warp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

public class Search {
    /**
     * Stores every warp object in memory
     */
    private static ArrayList<Warp> warps = new ArrayList<>();

    /**
     * Reads the warps database into memory
     */
    public static void read() {
        try (MongoCursor<Document> cursor = Phase.getCollection().sync().find(Filters.eq("expired", false))
                .sort(new BasicDBObject("visits", -1)).iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                warps.add(new Warp(document.getString("name"), document.getString("owner"), document.getInteger("visits"),
                        document.getString("category"), document.getBoolean("featured"), document.getBoolean("expired"),
                        document.getLong("renewed"), document.getDouble("x"), document.getDouble("y"),
                        document.getDouble("z"), document.getDouble("pitch"), document.getDouble("yaw"),
                        document.getString("world"), (ArrayList<String>) document.get("favorites"))); // TODO check cast
            }
        }
    }

    /**
     * Adds a warp object to the arraylist
     * @param warp Warp object to cache
     */
    public static void cacheWarp(final Warp warp) {
        warps.add(warp);
    }

    /**
     * Increments the visit count for a warp
     * @param warp Warp object to increment
     */
    public static void incrementVisits(final Warp warp) {
        getWarpByName(warp.getName()).getVisits().incrementAndGet();
        Phase.getCollection().sync().updateOne(Filters.eq("name", warp.getName()), new Document("$inc",
                new BasicDBObject().append("visits", 1)));
    }

    /**
     * Checks if a warp (public or private) exists in the database
     * @param name Warp name to check for
     * @return true if exists, false if doesn't
     */
    public static boolean exists(final String name) {
        return getWarpByName(name) != null;
    }

    /**
     * Removes a warp from the cache
     * @param warp Warp to delete
     */
    public static void remove(final Warp warp) {
        warps.remove(warp);
    }

    /**
     * Returns a public warp by name
     * @param name Warp name (case insensitive)
     * @return Warp object or null in the case of
     * a warp not being found
     */ // TODO figure out private warp handling with this
    public static Warp getWarpByName(final String name) {
        for (Warp warp : warps) {
            if (warp.getName().toLowerCase().equals(name.toLowerCase())) {
                return warp;
            }
        }
        return null;
    }

    /**
     * Returns every single currently created public warp
     * @return ArrayList of every warp object in the database
     */
    public static ArrayList<Warp> getPublicWarps() {
        sort();
        return (ArrayList<Warp>) warps.stream()
                .filter(warp -> !warp.isExpired())
                .collect(Collectors.toList());
    }

    /**
     * Returns a lost of all of the public featured warps
     * @return ArrayList of all public featured warps
     */
    public static ArrayList<Warp> getFeaturedWarps() {
        sort();
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
        sort();
        return (ArrayList<Warp>) warps.stream()
                .filter(warp -> warp.getOwnerUUID().equals(uuid))
                .collect(Collectors.toList());
    }

    private static void sort() {
        Collections.sort(warps, Collections.reverseOrder());
    }
}
