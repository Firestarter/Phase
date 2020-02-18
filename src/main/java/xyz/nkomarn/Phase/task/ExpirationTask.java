package xyz.nkomarn.Phase.task;

import com.mongodb.client.model.Filters;
import org.bson.Document;
import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.Phase.util.Search;

import java.util.concurrent.TimeUnit;

public class ExpirationTask implements Runnable {
    @Override
    public void run() {
        Search.getPublicWarps().forEach(warp -> {
            long days = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - warp.getRenewedTime());
            if (days >= 14) {
                warp.setExpired(true);
                System.out.println("Expired " + warp.getName());

                Phase.getCollection().sync().updateOne(Filters.eq("name", warp.getName()), new Document("$set",
                        new Document("expired", true))); // A S Y N C  PLZ
            }
        });
    }
}
