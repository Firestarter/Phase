package xyz.nkomarn.Phase.util;

import xyz.nkomarn.Phase.Phase;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import xyz.nkomarn.Kerosene.data.LocalStorage;

public class Database {
    public static boolean initialize() {
        Connection connection = null;
        try {
            connection = LocalStorage.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (connection == null) return false;

        final String query = "CREATE TABLE IF NOT EXISTS warps (id INTEGER PRIMARY KEY, name TEXT NOT NULL, owner TEXT NOT " +
                "NULL, visits INTEGER NOT NULL, category TEXT NOT NULL, featured BOOLEAN NOT NULL CHECK (featured IN (0,1)), " +
                "expired BOOLEAN NOT NULL CHECK (expired IN (0,1)), renewed INTEGER NOT NULL, x REAL NOT NULL, y REAL NOT " +
                "NULL, z REAL NOT NULL, pitch REAL NOT NULL, yaw REAL NOT NULL, world TEXT NOT NULL)";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
