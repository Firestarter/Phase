package xyz.nkomarn.Phase.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.nkomarn.Kerosene.data.LocalStorage;
import xyz.nkomarn.Phase.Phase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlayerJoinListener implements Listener {
    private final String UPDATE_QUERY = "UPDATE warps SET renewed = ?, expired = FALSE WHERE owner = ?;";

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(Phase.getPhase(), () -> {
            try (Connection connection = LocalStorage.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
                    statement.setLong(1, System.currentTimeMillis());
                    statement.setString(2, event.getPlayer().getUniqueId().toString());
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
