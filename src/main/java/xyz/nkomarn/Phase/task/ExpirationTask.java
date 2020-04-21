package xyz.nkomarn.Phase.task;

import xyz.nkomarn.Kerosene.data.LocalStorage;
import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.Phase.type.Warp;
import xyz.nkomarn.Phase.util.Search;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class ExpirationTask implements Runnable {
    @Override
    public void run() {
        Connection connection = null;

        try {
            connection = LocalStorage.getConnection();
            for (final Warp warp : Search.getPublicWarps()) {
                long days = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - warp.getRenewedTime());
                if (days >= 14) {
                    PreparedStatement statement = connection.prepareStatement("UPDATE warps SET expired =" +
                            " TRUE WHERE name LIKE ?;");
                    statement.setString(1, warp.getName());
                    statement.executeUpdate();
                    Phase.getPhase().getLogger().info(String.format("Warp '%s' expired.", warp.getName()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
