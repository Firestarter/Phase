package xyz.nkomarn.Phase.util;

import com.mongodb.BasicDBObject;
import org.bson.Document;
import xyz.nkomarn.Kerosene.database.subscribers.BasicSubscriber;
import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.Phase.type.Warp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Search {

    // TODO figure out private warp handling with this
    /**
     * Returns a public warp by name
     * @param name Warp name (case insensitive)
     * @return Warp object or null in the case of
     * a warp not being found
     */
    public static Warp getWarpByName(final String name) {
        try {
            ArrayList<Warp> warps = getPublicWarps().get();
            for (Warp warp : warps) {
                if (warp.getName().toLowerCase().equals(name.toLowerCase())) {
                    return warp;
                }
            }
            return null;
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    /**
     * Returns every single currently created public warp
     * @return HashSet of every warp object in the database
     */
    public static Future<ArrayList<Warp>> getPublicWarps() {
        CompletableFuture<ArrayList<Warp>> future = new CompletableFuture<>();
        ArrayList<Warp> warps = new ArrayList<>();
        Phase.getCollection().async().find().sort(new BasicDBObject("visits", -1)).subscribe(
                new BasicSubscriber<Document>() {
            public void onNext(Document document) {
                warps.add(new Warp(document.getString("name"), document.getString("owner"), document.getInteger("visits"),
                        document.getBoolean("type"), document.getString("category"), document.getBoolean("featured"),
                        document.getBoolean("expired"), document.getLong("renewed"), document.getDouble("x"),
                        document.getDouble("y"), document.getDouble("z"), document.getDouble("yaw"), document.getDouble("pitch"),
                        document.getString("world"), (String[]) document.get("favorites")));
            }
        });
        future.complete(warps);
        return future;
    }
}
