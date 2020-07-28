package xyz.nkomarn.Phase.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.kerosene.Kerosene;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlayerJoinListener implements Listener {

    private static final String UPDATE_QUERY = "UPDATE warps SET renewed = ?, expired = FALSE WHERE owner = ?;";

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Kerosene.getPool().submit(() -> {
            try (Connection connection = Phase.getStorage().getConnection()) {
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
