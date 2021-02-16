package xyz.nkomarn.Phase.util;

import xyz.nkomarn.Phase.Phase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {
    private static final String TABLE_QUERY = "CREATE TABLE IF NOT EXISTS warps (id INTEGER PRIMARY KEY, name TEXT NOT " +
            "NULL, owner TEXT NOT NULL, visits INTEGER NOT NULL, category TEXT NOT NULL, featured BOOLEAN NOT NULL " +
            "CHECK (featured IN (0,1)), expired BOOLEAN NOT NULL CHECK (expired IN (0,1)), renewed INTEGER NOT NULL, x " +
            " NOT NULL, y REAL NOT NULL, z REAL NOT NULL, pitch REAL NOT NULL, yaw REAL NOT NULL, world TEXT NOT NULL, description TEXT NOT NULL)";

    public static boolean initialize() {
        try (Connection connection = Phase.getStorage().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(TABLE_QUERY);) {
                statement.execute();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
