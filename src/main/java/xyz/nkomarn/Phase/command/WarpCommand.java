package xyz.nkomarn.Phase.command;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import xyz.nkomarn.Phase.gui.inventory.MainMenu;
import xyz.nkomarn.Phase.type.Warp;
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
            return true;
        }

        // TODO parse input and warp
        StringBuilder warpNameBuilder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            warpNameBuilder.append(args[i]).append(" ");
        }
        final String warpName = warpNameBuilder.toString().trim();

        if (!Search.exists(warpName)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                    "%sWarp '%s' doesn't exist.", prefix, warpName
            )));
            return true;
        }

        final Warp warp = Search.getWarpByName(warpName);
        WarpUtil.warp(player, warp); // Convert to null check instead of using exists()
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        StringBuilder arguments = new StringBuilder();
        for (String arg : args) {
            arguments.append(arg).append(" ");
        }
        ArrayList<String> queryResults = new ArrayList<>();
        Search.getPublicWarps().stream()
                .filter(warp -> warp.getName().toLowerCase().contains(arguments.toString().trim()))
                .collect(Collectors.toList()).forEach(warp -> queryResults.add(warp.getName()));
        return queryResults;
    }
}
