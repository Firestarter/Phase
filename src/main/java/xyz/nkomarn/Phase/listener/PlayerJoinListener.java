package xyz.nkomarn.Phase.listener;

import com.firestartermc.kerosene.util.ConcurrentUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.nkomarn.Phase.Phase;

public class PlayerJoinListener implements Listener {

    private static final String UPDATE_QUERY = "UPDATE warps SET renewed = ?, expired = FALSE WHERE owner = ?;";

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ConcurrentUtils.callAsync(() -> {
            var connection = Phase.getStorage().getConnection();
            var statement = connection.prepareStatement(UPDATE_QUERY);
            statement.setLong(1, System.currentTimeMillis());
            statement.setString(2, event.getPlayer().getUniqueId().toString());

            try (connection; statement) {
                statement.executeUpdate();
            }

            return null;
        });
    }
}
