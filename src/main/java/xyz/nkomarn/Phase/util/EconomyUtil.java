package xyz.nkomarn.Phase.util;

import org.bukkit.entity.Player;
import xyz.nkomarn.Phase.Phase;

public class EconomyUtil {

    /**
     * Returns the player's current account balance (using Vault API)
     * @param player The player to check balance for
     * @return The player's current account balance
     */
    public static double getBalance(final Player player) {
        double balance = 0.0;
        try {
            balance = Phase.getEconomy().getBalance(player);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return balance;
    }

    /**
     * Withdraws an amount from a player's balance
     * @param player The player to withdraw money from
     * @param amount Amount to withdraw
     */
    public static void withdraw(final Player player, final double amount) {
        Phase.getEconomy().withdrawPlayer(player, amount);
    }
}
