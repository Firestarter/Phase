package xyz.nkomarn.Phase.gui.handler;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import xyz.nkomarn.Kerosene.util.ClaimUtil;
import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.Phase.gui.GUIHolder;
import xyz.nkomarn.Phase.gui.inventory.CategoryPicker;
import xyz.nkomarn.Phase.type.Warp;
import xyz.nkomarn.Phase.util.Config;
import xyz.nkomarn.Phase.util.Search;
import xyz.nkomarn.Phase.util.WarpUtil;

public class OptionsHandler implements GUIHandler {
    final String prefix = Config.getString("messages.prefix");

    @Override
    public void handle(Player player, int slot, InventoryClickEvent event) {
        final GUIHolder holder = (GUIHolder) event.getInventory().getHolder();
        final Warp warp = Search.getWarpByName(holder.getData().toLowerCase());
        if (warp == null) return;
        player.closeInventory();

        if (slot == 2) {
            Bukkit.getScheduler().runTaskAsynchronously(Phase.getPhase(), () -> WarpUtil.warpPlayer(player, warp));
        } else if (slot == 3) {
            new AnvilGUI.Builder()
                    .onComplete((p, text) -> {
                        Warp existingWarp = Search.getWarpByName(text);
                        if (existingWarp != null) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                                    "%sA warp by the name '%s' already exists.", prefix, text
                            )));
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                            return AnvilGUI.Response.close();
                        }

                        Bukkit.getScheduler().runTaskAsynchronously(Phase.getPhase(), () ->
                                WarpUtil.renameWarp(warp.getName(), ChatColor.stripColor(text)));
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, 1.0f);
                        player.sendTitle(ChatColor.translateAlternateColorCodes('&',
                                "&6&lRenamed warp."), ChatColor.translateAlternateColorCodes('&',
                                String.format("&fIt's now called '%s'.", text)), 10, 70, 20);
                        return AnvilGUI.Response.close();
                    })
                    .title("Rename your Warp")
                    .text(ChatColor.translateAlternateColorCodes('&',
                            "Enter a new name."))
                    .item(new ItemStack(Material.BOOK))
                    .plugin(Phase.getPhase())
                    .open(player);
        } else if (slot == 4) {
            if (ClaimUtil.doesLocationHaveForeignClaims(player, player.getLocation())) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                        "%sYou can't move warps to others' claims.", prefix
                )));
            } else {
                WarpUtil.relocateWarp(warp.getName(), player.getLocation());
                player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6&lRelocated"),
                        ChatColor.translateAlternateColorCodes('&', "The warp is now at your location."));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, 1.0f);
            }
        } else if (slot == 5) {
            new CategoryPicker(player, warp);
        } else if (slot == 6) {
            WarpUtil.delete(warp.getName());
            player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&c&lDeleted"),
                    ChatColor.translateAlternateColorCodes('&', "The warp was blown away with the wind."));
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
        }
    }
}
