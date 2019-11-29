package xyz.nkomarn.Phase;

import net.milkbowl.vault.economy.Economy;
import org.bson.Document;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.nkomarn.Kerosene.database.Database;
import xyz.nkomarn.Kerosene.database.SyncAsyncCollection;

public class Phase extends JavaPlugin {
    private static Phase instance;
    private static SyncAsyncCollection<Document> warps;
    private static Economy economy = null;

    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        initializeEconomy();

        final String database = getConfig().getString("database");
        warps = Database.getSyncAsyncCollection(database, "warps");
    }

    public void onDisable() {

    }

    public static Phase getInstance() {
        return instance;
    }

    public static SyncAsyncCollection<Document> getCollection() {
        return warps;
    }

    public static Economy getEconomy() {
        return economy;
    }

    private boolean initializeEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().severe("Phase requires Vault to operate.");
            getServer().getPluginManager().disablePlugin(instance);
        }
        RegisteredServiceProvider<Economy> provider = getServer().getServicesManager()
                .getRegistration(Economy.class);
        if (provider == null) return false;
        economy = provider.getProvider();
        return true;
    }
}
