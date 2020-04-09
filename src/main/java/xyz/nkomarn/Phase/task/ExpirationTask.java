package xyz.nkomarn.Phase.task;

import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.Phase.util.Search;

import java.util.concurrent.TimeUnit;

public class ExpirationTask implements Runnable {
    @Override
    public void run() {
        Search.getPublicWarps().stream().parallel().forEach(warp -> {
            long days = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - warp.getRenewedTime());
            if (days >= 14) {
                warp.setExpired(true);
                Phase.getPhase().getLogger().info(String.format("Warp '%s' expired.", warp.getName()));
               /* Phase.getCollection().sync().updateOne(Filters.eq("name", warp.getName()), new Document("$set",
                        new Document("expired", true))); // A S Y N C  PLZ*/
            }
        });
    }
}
