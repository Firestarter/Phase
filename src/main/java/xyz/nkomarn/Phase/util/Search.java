package xyz.nkomarn.Phase.util;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.Phase.type.Warp;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Search {

    /**
     * Checks if a warp (public or private) exists in the database
     *
     * @param name Warp name to check for
     * @return true if exists, false if doesn't
     */
    public static boolean exists(final String name) {
        return getWarpByName(name) != null;
    }

    // TODO figure out private warp handling with this

    /**
     * Returns a public warp by name
     *
     * @param name Warp name (case insensitive)
     * @return Warp object or null in the case of
     * a warp not being found
     */
    public static Warp getWarpByName(final String name) {
        ArrayList<Warp> warps = getPublicWarps();
        for (Warp warp : warps) {
            if (warp.getName().toLowerCase().equals(name.toLowerCase())) {
                return warp;
            }
        }
        return null;
    }

    /**
     * Returns every single currently created public warp
     *
     * @return HashSet of every warp object in the database
     */
    public static ArrayList<Warp> getPublicWarps() {
        ArrayList<Warp> warps = new ArrayList<>();
        MongoCursor<Document> cursor = Phase.getCollection().sync().find(Filters.and(Filters.eq("expired", false),
                Filters.eq("type", true))).sort(new BasicDBObject("visits", -1)).iterator();

        try {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                warps.add(new Warp(document.getString("name"), document.getString("owner"), document.getInteger("visits"),
                    document.getBoolean("type"), document.getString("category"), document.getBoolean("featured"),
                    document.getBoolean("expired"), document.getLong("renewed"), document.getDouble("x"),
                    document.getDouble("y"), document.getDouble("z"), document.getDouble("pitch"), document.getDouble("yaw"),
                    document.getString("world"), (ArrayList<String>) document.get("favorites"))); // TODO check cast
            }
        } finally {
            cursor.close();
            return warps;
        }
    }
}
