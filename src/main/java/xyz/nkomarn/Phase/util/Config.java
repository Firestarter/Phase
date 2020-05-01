package xyz.nkomarn.Phase.util;

import xyz.nkomarn.Phase.Phase;

import java.util.ArrayList;
import java.util.List;

public class Config {
    /**
     * Fetches the message prefix from the configuration
     */
    public static String getPrefix() {
        return getString("messages.prefix");
    }

    /**
     * Fetches a boolean from the configuration
     * if location is not found, <code>false</code> is returned
     * @param location Configuration location of the boolean
     */
    public static boolean getBoolean(String location) {
        return Phase.getPhase().getConfig().getBoolean(location, false);
    }

    /**
     * Fetches a string from the configuration
     * if location is not found, <code>empty string</code> is returned
     * @param location Configuration location of the string
     */
    public static String getString(String location) {
        return Phase.getPhase().getConfig().getString(location, "");
    }

    /**
     * Fetches an integer from the configuration
     * if location is not found, <code>0</code> is returned
     * @param location Configuration location of the integer
     */
    public static int getInteger(String location) {
        return Phase.getPhase().getConfig().getInt(location, 0);
    }

    /**
     * Fetches a double from the configuration
     * if location is not found, <code>0.0</code> is returned
     * @param location Configuration location of the double
     */
    public static double getDouble(String location) {
        return Phase.getPhase().getConfig().getDouble(location, 0.0);
    }

    /**
     * Fetches a list from the configuration
     * if location is not found, <code>empty list</code> is returned
     * @param location Configuration location of the list
     */
    public static List<String> getList(String location) {
        return (List<String>) Phase.getPhase().getConfig().getList(location, new ArrayList<>());
    }
}
