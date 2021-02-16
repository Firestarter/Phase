package xyz.nkomarn.Phase.util;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.Phase.type.Category;
import xyz.nkomarn.Phase.type.Warp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Cache {

    private final Phase phase;
    private final Map<String, Warp> warps;

    public Cache(@NotNull Phase phase) {
        this.phase = phase;
        this.warps = new HashMap<>();
    }

    public void cache() throws SQLException {
        Connection connection = phase.getStorage().getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM warps;");
        ResultSet result = statement.executeQuery();

        try (connection; statement; result) {
            //resultToList(result).forEach(warp -> warps.put(warp.getName().toLowerCase(), warp));
        }
    }

    public void createWarp(@NotNull String name, @NotNull UUID owner, @NotNull Location location, @NotNull Category category, int visits) {
        Warp warp = new Warp(
                name,
                owner,
                location,
                category,
                visits,
                false,
                false,
                System.currentTimeMillis(),
                "uwu"
        );

        warps.put(name.toLowerCase(), warp);

    }

    public void update(Warp warp) {

    }

}
