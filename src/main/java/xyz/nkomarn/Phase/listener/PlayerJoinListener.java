package xyz.nkomarn.Phase.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.nkomarn.Kerosene.data.LocalStorage;
import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.Phase.util.Search;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(Phase.getPhase(), () -> {
            Connection connection = null;

            try {
                connection = LocalStorage.getConnection();
                PreparedStatement statement = connection.prepareStatement("UPDATE warps SET renewed = ?, " +
                        "expired = FALSE WHERE owner = ?;");
                statement.setLong(1, System.currentTimeMillis());
                statement.setString(2, event.getPlayer().getUniqueId().toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });
    }
}
