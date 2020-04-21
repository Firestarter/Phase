package xyz.nkomarn.Phase.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.Phase.gui.inventory.MainMenu;
import xyz.nkomarn.Phase.type.Warp;
import xyz.nkomarn.Phase.util.Advancements;
import xyz.nkomarn.Phase.util.Config;
import xyz.nkomarn.Phase.util.Search;
import xyz.nkomarn.Phase.util.WarpUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WarpCommand implements TabExecutor {
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
            new MainMenu(player);
            player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
            if (!Advancements.isComplete(player, "warp-menu")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("advancement grant %s only firestarter:warp-menu",
                        player.getName()));
            }
            return true;
        }

        final StringBuilder warpNameBuilder = new StringBuilder();
        for (String arg : args) warpNameBuilder.append(arg).append(" ");
        final String warpName = warpNameBuilder.toString().trim();

        Bukkit.getScheduler().runTaskAsynchronously(Phase.getPhase(), () -> {
            final Warp warp = Search.getWarpByName(warpName);
            if (warp == null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                        "%sWarp '%s' doesn't exist.", prefix, warpName
                )));
            } else {
                WarpUtil.warp(player, warp);
            }
        });
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        StringBuilder arguments = new StringBuilder();
        for (String arg : args) arguments.append(arg).append(" ");

        ArrayList<String> queryResults = new ArrayList<>();
        Search.getPublicWarps().stream()
                .filter(warp -> warp.getName().toLowerCase().contains(arguments.toString().trim()))
                .collect(Collectors.toList()).forEach(warp -> queryResults.add(warp.getName()));
        return queryResults;
    }
}
