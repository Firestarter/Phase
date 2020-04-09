package xyz.nkomarn.Phase;

import com.earth2me.essentials.Essentials;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.nkomarn.Phase.command.SetWarpCommand;
import xyz.nkomarn.Phase.command.WarpAdminCommand;
import xyz.nkomarn.Phase.command.WarpCommand;
import xyz.nkomarn.Phase.listener.InventoryClickListener;
import xyz.nkomarn.Phase.listener.PlayerJoinListener;
import xyz.nkomarn.Phase.util.Database;

public class Phase extends JavaPlugin {
    private static Phase phase;
    private static Economy economy = null;
    private static Essentials essentials;

    public void onEnable() {
        phase = this;
        saveDefaultConfig();

        if (!Database.initialize()) {
            getLogger().severe("Failed to initialize the database.");
            getServer().getPluginManager().disablePlugin(this);
        }

        if (!initializeEconomy()) {
            getLogger().warning("Couldn't initialize economy.");
            return;
        }

        /*final String database = getConfig().getString("database");
        warps = MongoDatabase.getFlexibleCollection(database, "warps");
        Search.read();*/

        PluginCommand warpCommand = getCommand("warp");
        warpCommand.setExecutor(new WarpCommand());
        warpCommand.setTabCompleter(new WarpCommand());
        PluginCommand warpAdminCommand = getCommand("warpadmin");
        warpAdminCommand.setExecutor(new WarpAdminCommand());
        warpAdminCommand.setTabCompleter(new WarpAdminCommand());
        PluginCommand setWarpCommand = getCommand("setwarp");
        setWarpCommand.setExecutor(new SetWarpCommand());
        setWarpCommand.setTabCompleter(new SetWarpCommand());

        essentials = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
        //getServer().getScheduler().runTaskTimerAsynchronously(this, Search::sort, 0, 60 * 20);
       // getServer().getScheduler().runTaskTimer(this, new ExpirationTask(), 0, 10 * 20);
    }

    public void onDisable() { }

    public static Phase getPhase() {
        return phase;
    }

    public static Economy getEconomy() {
        return economy;
    }

    public static Essentials getEssentials() {
        return essentials;
    }

    private boolean initializeEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().severe("Phase requires Vault to operate.");
            getServer().getPluginManager().disablePlugin(phase);
        }
        RegisteredServiceProvider<Economy> provider = getServer().getServicesManager()
                .getRegistration(Economy.class);
        if (provider == null) return false;
        economy = provider.getProvider();
        return true;
    }
}
