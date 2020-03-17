package xyz.nkomarn.Phase.gui.handler;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
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

        player.closeInventory();
        final Warp warp = Search.getWarpByName(holder.getData().toLowerCase());
        if (warp == null) return;

        if (slot == 2) {
            WarpUtil.warp(player, warp);
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

                        WarpUtil.rename(warp, ChatColor.stripColor(text));
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, 1.0f);
                        player.sendTitle(ChatColor.translateAlternateColorCodes('&',
                                "&6&lRenamed warp."), ChatColor.translateAlternateColorCodes('&',
                                String.format("&fIt's now called '%s'.", text)));
                        return AnvilGUI.Response.close();
                    })
                    .title("Rename your Warp")
                    .text(ChatColor.translateAlternateColorCodes('&',
                            "Enter a new name."))
                    .item(new ItemStack(Material.BOOK))
                    .plugin(Phase.getPhase())
                    .open(player);

            /*player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                    "%sThis feature is currently not implemented. Check back in a few days.", Config.getPrefix()
            )));*/
        } else if (slot == 4) {
            WarpUtil.relocate(player, warp);
        } else if (slot == 5) {
            new CategoryPicker(player, warp);
            /*player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(
                    "%sThis feature is currently not implemented. Check back in a few days.", Config.getPrefix()
            )));*/
        } else if (slot == 6) {
            WarpUtil.delete(player, warp);
        }
    }
}
