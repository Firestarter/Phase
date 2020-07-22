package xyz.nkomarn.Phase;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.nkomarn.Phase.command.SetWarpCommand;
import xyz.nkomarn.Phase.command.WarpAdminCommand;
import xyz.nkomarn.Phase.command.WarpCommand;
import xyz.nkomarn.Phase.listener.InventoryClickListener;
import xyz.nkomarn.Phase.listener.PlayerJoinListener;
import xyz.nkomarn.Phase.listener.SignListener;
import xyz.nkomarn.Phase.task.ExpirationTask;
import xyz.nkomarn.Phase.util.Database;
import xyz.nkomarn.kerosene.data.db.LocalStorage;

public class Phase extends JavaPlugin {

    private static Phase PHASE;
    private static LocalStorage STORAGE;

    public void onEnable() {
        PHASE = this;
        saveDefaultConfig();

        STORAGE = new LocalStorage("phase");

        if (!Database.initialize()) {
            getLogger().severe("Failed to initialize the database.");
            getServer().getPluginManager().disablePlugin(this);
        }

        PluginCommand warpCommand = getCommand("warp");
        warpCommand.setExecutor(new WarpCommand());
        warpCommand.setTabCompleter(new WarpCommand());
        PluginCommand warpAdminCommand = getCommand("warpadmin");
        warpAdminCommand.setExecutor(new WarpAdminCommand());
        warpAdminCommand.setTabCompleter(new WarpAdminCommand());
        PluginCommand setWarpCommand = getCommand("setwarp");
        setWarpCommand.setExecutor(new SetWarpCommand());

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new SignListener(), this);
        getServer().getScheduler().runTaskTimerAsynchronously(this, new ExpirationTask(), 0, 10 * 20);
    }

    public void onDisable() {
    }

    public static Phase getPhase() {
        return PHASE;
    }

    public static LocalStorage getStorage() {
        return STORAGE;
    }
}
