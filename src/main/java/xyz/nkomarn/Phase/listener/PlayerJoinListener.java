package xyz.nkomarn.Phase.listener;

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
                System.out.println("Renewing " + event.getPlayer().getName() + "'s warps.");
                Search.getPlayerWarps(event.getPlayer().getUniqueId()).forEach(warp -> {
                    System.out.println("Renewed " + warp.getName());
                    warp.setRenewed(System.currentTimeMillis());
                    warp.setExpired(false);
                });
            }
        }.runTaskAsynchronously(Phase.getPhase());
    }
}
