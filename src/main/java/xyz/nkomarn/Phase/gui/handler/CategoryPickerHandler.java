package xyz.nkomarn.Phase.gui.handler;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import xyz.nkomarn.Phase.gui.GUIHolder;
import xyz.nkomarn.Phase.type.Warp;
import xyz.nkomarn.Phase.util.Search;
import xyz.nkomarn.Phase.util.WarpUtil;

public class CategoryPickerHandler implements GUIHandler {
    @Override
    public void handle(Player player, int slot, InventoryClickEvent event) {
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.WHITE_STAINED_GLASS_PANE) return;
        final String category = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());

        final Warp warp = Search.getWarpByName(((GUIHolder) event.getClickedInventory().getHolder()).getData());
        if (warp == null) return; // TODO check holder and error messages
        WarpUtil.changeCategory(warp, category);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0f, 1.0f);
        player.sendTitle(ChatColor.translateAlternateColorCodes('&',
                "&6&lChanged category."), ChatColor.translateAlternateColorCodes('&',
                String.format("&fThe category is now '%s'.", category)));
        player.closeInventory();
    }
}
