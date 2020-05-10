package xyz.nkomarn.Phase.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import xyz.nkomarn.Kerosene.util.AdvancementUtil;
import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.Phase.gui.inventory.MainMenu;
import xyz.nkomarn.Phase.type.Warp;
import xyz.nkomarn.Phase.util.Config;
import xyz.nkomarn.Phase.util.Search;
import xyz.nkomarn.Phase.util.WarpUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WarpCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        if (args.length < 1) {
            new MainMenu(player);
            player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
            AdvancementUtil.grantAdvancement(player, "warp-menu");
        } else {
            String warpName = WarpUtil.argsToString(args);

            Bukkit.getScheduler().runTaskAsynchronously(Phase.getPhase(), () -> {
                Optional<Warp> warp = Search.getWarpByName(warpName);
                if (warp.isEmpty()) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                            "%sWarp '%s' doesn't exist.", Config.getPrefix(), warpName
                    )));
                } else {
                    WarpUtil.warpPlayer(player, warp.get());
                }
            });
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        ArrayList<String> queryResults = new ArrayList<>();
        Search.getPublicWarps().stream()
                .filter(warp -> warp.getName().toLowerCase().contains(WarpUtil.argsToString(args).toLowerCase()))
                .map(warp -> ChatColor.stripColor(warp.getName()))
                .forEach(queryResults::add);
        return queryResults;
    }
}
