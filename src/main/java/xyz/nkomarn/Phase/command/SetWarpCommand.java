package xyz.nkomarn.Phase.command;

import com.firestartermc.kerosene.Kerosene;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.Phase.type.Category;
import xyz.nkomarn.Phase.type.Warp;
import xyz.nkomarn.Phase.util.ClaimUtils;
import xyz.nkomarn.Phase.util.Config;
import xyz.nkomarn.Phase.util.Search;
import xyz.nkomarn.Phase.util.WarpUtil;

import java.text.DecimalFormat;
import java.util.Optional;
import java.util.regex.Pattern;

public class SetWarpCommand implements CommandExecutor {
    private final Pattern PATTERN = Pattern.compile("^[0-9A-Za-z\\s-]+$");
    private final int CREATION_COST = Config.getInteger("economy.create");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                    "%sCreate a warp using /setwarp [warp name].", Config.getPrefix()
            )));
            return true;
        } else {
            String warpName = WarpUtil.argsToString(args);

            Bukkit.getScheduler().runTaskAsynchronously(Phase.getPhase(), () -> {
                Optional<Warp> warp = Search.getWarpByName(warpName);
                Location warpLocation = player.getLocation();
                var economy = Kerosene.getKerosene().getEconomy();

                if (warp.isPresent()) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                            "%sA warp with the name '%s' already exists.", Config.getPrefix(), warpName
                    )));
                } else if (warpName.length() > 20) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                            "%sWarp names are limited to 20 characters in length.", Config.getPrefix()
                    )));
                } else if (!PATTERN.matcher(warpName).matches()) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                            "%sWarp names must be alphanumeric.", Config.getPrefix()
                    )));
                } else if (ClaimUtils.isForeignClaim(player, warpLocation)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                            "%sYou can't create warps in others' claims.", Config.getPrefix()
                    )));
                } else if (economy.getBalance(player) < CREATION_COST) {
                    DecimalFormat formatter = new DecimalFormat("#,###");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                            "%sYou need to have $%s available to create a warp.",
                            Config.getPrefix(), formatter.format(CREATION_COST)
                    )));
                } else {
                    WarpUtil.createWarp(warpName, player.getUniqueId(), warpLocation, Category.ALL);
                    economy.withdrawPlayer(player, CREATION_COST);
                    player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6&lCreated warp."),
                            ChatColor.translateAlternateColorCodes('&', "&fCreated a warp in this location."));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                            "%sCreated warp '%s' in your location!", Config.getPrefix(), warpName
                    )));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, 1.0f);
                }
            });
        }
        return true;
    }
}
