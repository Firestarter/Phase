package xyz.nkomarn.Phase.gui.handler;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import xyz.nkomarn.Phase.gui.GuiHolder;
import xyz.nkomarn.Phase.type.Category;
import xyz.nkomarn.Phase.type.Warp;
import xyz.nkomarn.Phase.util.Search;
import xyz.nkomarn.Phase.util.WarpUtil;

import java.util.Optional;

public class CategoryPickerHandler implements GuiHandler {
    @Override
    public void handle(Player player, int slot, InventoryClickEvent event) {
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.WHITE_STAINED_GLASS_PANE) return;
        Category category = Category.valueOf(ChatColor.stripColor(event.getCurrentItem().getItemMeta()
                .getDisplayName()).toUpperCase());

        if (event.getClickedInventory() == null) return;
        Optional<Warp> warp = Search.getWarpByName(((GuiHolder) event.getClickedInventory().getHolder()).getData());
        if (warp.isEmpty()) return;
        WarpUtil.changeWarpCategory(warp.get().getName(), category);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0f, 1.0f);
        player.sendTitle(ChatColor.translateAlternateColorCodes('&',
                "&6&lChanged category."), ChatColor.translateAlternateColorCodes('&',
                String.format("&fThe category is now '%s'.", category.getName())));
        player.closeInventory();
    }
}
