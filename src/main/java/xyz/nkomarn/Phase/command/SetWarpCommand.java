package xyz.nkomarn.Phase.command;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import xyz.nkomarn.Phase.type.Warp;
import xyz.nkomarn.Phase.util.Config;
import xyz.nkomarn.Phase.util.EconomyUtil;
import xyz.nkomarn.Phase.util.Search;
import xyz.nkomarn.Phase.util.WarpUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SetWarpCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        final String prefix = Config.getString("messages.prefix");

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                    "%sYou have to be a player to use warps. Sorry console :(", prefix
            )));
            return true;
        }

        final Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                    "%sUsage: /setwarp <name>", prefix
            )));
            return true;
        }

        StringBuilder warpNameBuilder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            warpNameBuilder.append(args[i]).append(" ");
        }
        final String warpName = warpNameBuilder.toString().trim();

        if (Search.exists(warpName)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                    "%sA warp with the name '%s' already exists.", prefix, warpName
            )));
            return true;
        }

        final int creationCost = Config.getInteger("economy.create");
        if (EconomyUtil.getBalance(player) < creationCost) {
            DecimalFormat formatter = new DecimalFormat("#,###");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                    "%sYou need to have $%s available to create a warp.", prefix, formatter.format(creationCost)
            )));
            return true;
        }

        // FIXME temporary insertion testing code\
        final Location location = player.getLocation();
        final Warp warp = new Warp(warpName, player.getUniqueId().toString(), 0, true, "Grinder", true,
                false, System.currentTimeMillis(), location.getX(), location.getY(), location.getZ(), location.getPitch(),
                location.getYaw(), location.getWorld().getUID().toString(), new ArrayList<>());
        WarpUtil.createWarp(warp);
        EconomyUtil.withdraw(player, creationCost);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                "%sCreated warp '%s'!", prefix, warpName
        )));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        return null;
    }
}
