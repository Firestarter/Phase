package xyz.nkomarn.Phase.task;

import xyz.nkomarn.Kerosene.data.LocalStorage;
import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.Phase.util.Search;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class ExpirationTask implements Runnable {
    private final String EXPIRATION_QUERY = "UPDATE warps SET expired = TRUE WHERE name LIKE ?;";

    @Override
    public void run() {
        try (Connection connection = LocalStorage.getConnection()) {
            Search.getPublicWarps().forEach(warp -> {
                if (TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - warp.getRenewedTime()) >= 14) {
                    Phase.getPhase().getLogger().info(String.format("Warp '%s' expired.", warp.getName()));
                    try (PreparedStatement statement = connection.prepareStatement(EXPIRATION_QUERY)) {
                        statement.setString(1, warp.getName());
                        statement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
