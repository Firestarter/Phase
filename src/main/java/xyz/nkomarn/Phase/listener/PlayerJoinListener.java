package xyz.nkomarn.Phase.listener;

import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.Phase.util.Search;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Search.getPlayerWarps(event.getPlayer().getUniqueId()).forEach(warp -> {
                    warp.setRenewed(System.currentTimeMillis());
                    warp.setExpired(false);
                    Phase.getCollection().sync().updateOne(Filters.eq("name", warp.getName()), new Document("$set",
                            new Document("renewed", System.currentTimeMillis()))); // A S Y N C  PLZ
                    Phase.getCollection().sync().updateOne(Filters.eq("name", warp.getName()), new Document("$set",
                            new Document("expired", false))); // A S Y N C  PLZ
                });
            }
        }.runTaskAsynchronously(Phase.getPhase());
    }
}
