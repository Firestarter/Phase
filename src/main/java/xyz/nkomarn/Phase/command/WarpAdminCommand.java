package xyz.nkomarn.Phase.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import xyz.nkomarn.Phase.type.Warp;
import xyz.nkomarn.Phase.util.Config;
import xyz.nkomarn.Phase.util.Search;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

public class WarpAdminCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("phase.admin")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                    "%sInsufficient permissions.", Config.getPrefix()
            )));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                    "%sSpecify an operation.", Config.getPrefix()
            )));
            return true;
        }

        final String operation = args[0].toLowerCase();
        if (operation.equals("info")) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                        "%sSpecify a warp to return info for.", Config.getPrefix()
                )));
                return true;
            }

            StringBuilder warpNameBuilder = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                warpNameBuilder.append(args[i]).append(" ");
            }
            final String warpName = warpNameBuilder.toString().trim();

            final Warp warp = Search.getWarpByName(warpName);
            if (warp == null) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                        "%sA warp by that name does not exist.", Config.getPrefix()
                )));
                return true;
            }

            StringBuilder information = new StringBuilder();
            information.append(String.format("%sInfo for warp '%s': .\n", Config.getPrefix(), warp.getName()));
            final OfflinePlayer owner = Bukkit.getOfflinePlayer(warp.getOwnerUUID());
            information.append(String.format(ChatColor.GRAY + "Owner: %s (%s)\n", owner.getName(), warp.getOwnerUUID().toString()));
            final Location warpLocation = warp.getLocation();
            information.append(String.format(ChatColor.GRAY + "X: %s, Y: %s, Z: %s, pitch: %s, yaw: %s, world: %s\n",
                    warpLocation.getX(), warpLocation.getY(), warpLocation.getZ(), warpLocation.getPitch(),
                    warpLocation.getYaw(), warpLocation.getWorld().getName()));
            information.append(String.format(ChatColor.GRAY + "Featured: %s\n", warp.isFeatured()));
            information.append(String.format(ChatColor.GRAY + "Expired: %s\n", warp.isExpired()));
            information.append(String.format(ChatColor.GRAY + "Last renewed: %s\n", warp.getRenewedTime()));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', information.toString()));
            return true;
        } else if (operation.equals("feature")) {
           // TODO
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("phase.admin")) return null;
        if (args.length != 1) return null;
        return Arrays.asList("create", "feature", "info", "delete");
    }
}
