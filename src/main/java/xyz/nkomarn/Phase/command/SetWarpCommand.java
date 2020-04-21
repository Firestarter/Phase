package xyz.nkomarn.Phase.command;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.Phase.type.Warp;
import xyz.nkomarn.Phase.util.Config;
import xyz.nkomarn.Phase.util.EconomyUtil;
import xyz.nkomarn.Phase.util.Search;
import xyz.nkomarn.Phase.util.WarpUtil;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

public class SetWarpCommand implements TabExecutor {
    final String prefix = Config.getString("messages.prefix");
    final int creationCost = Config.getInteger("economy.create");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                    "%sYou have to be a player to use warps.", prefix
            )));
            return true;
        }

        final Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                    "%sUsage: /setwarp [warp name]", prefix
            )));
            return true;
        }

        final StringBuilder warpNameBuilder = new StringBuilder();
        for (String arg : args) warpNameBuilder.append(arg).append(" ");
        final String warpName = warpNameBuilder.toString().trim();

        Bukkit.getScheduler().runTaskAsynchronously(Phase.getPhase(), () -> {
            final Warp warp = Search.getWarpByName(warpName);
            if (warp != null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                        "%sA warp with the name '%s' already exists.", prefix, warpName
                )));
            } else {
                final Location warpLocation = player.getLocation();

                if (!StringUtils.isAlphanumeric(warpName)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                            "%sWarp names must be alphanumeric.", prefix
                    )));
                } else if (WarpUtil.locationHasClaims(player, warpLocation)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                            "%sYou can't create warps in others' claims.", prefix
                    )));
                } else if (EconomyUtil.getBalance(player) < creationCost) {
                    DecimalFormat formatter = new DecimalFormat("#,###");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                            "%sYou need to have $%s available to create a warp.", prefix, formatter.format(creationCost)
                    )));
                } else {
                    final Warp newWarp = new Warp(warpName, player.getUniqueId().toString(), 0, "All",
                            false, false, System.currentTimeMillis(), warpLocation.getX(),
                            warpLocation.getY(), warpLocation.getZ(), warpLocation.getPitch(), warpLocation.getYaw(),
                            warpLocation.getWorld().getUID().toString());

                    try {
                        WarpUtil.createWarp(newWarp);
                        EconomyUtil.withdraw(player, creationCost);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                                "%sCreated warp '%s' in your location!", prefix, warpName
                        )));
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, 1.0f);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                                "%s&eAn error occurred while creating your warp- notify an admin.", prefix
                        )));
                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                    }
                }
            }
        });

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        return null;
    }
}
