package xyz.nkomarn.Phase.gui.handler;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import xyz.nkomarn.Phase.gui.inventory.FeaturedWarps;
import xyz.nkomarn.Phase.gui.inventory.PlayerWarps;
import xyz.nkomarn.Phase.gui.inventory.PublicWarps;
import xyz.nkomarn.Phase.util.Config;

public class MainMenuHandler implements GUIHandler {
    @Override
    public void handle(Player player, int slot, InventoryClickEvent event) {
        if (slot == 10) {
            ItemStack guideBook = new ItemStack(Material.WRITTEN_BOOK, 1);
            BookMeta guideBookMeta = (BookMeta) guideBook.getItemMeta();
            guideBookMeta.setTitle(Config.getString("guide.title"));
            guideBookMeta.setAuthor(Config.getString("guide.author"));
            guideBookMeta.setPages(Config.getList("guide.pages"));
            guideBook.setItemMeta(guideBookMeta);
            player.openBook(guideBook);
        } else if (slot == 12) {
            new FeaturedWarps(player, 1);
        } else if (slot == 13) {
            new PublicWarps(player, 1);
        } else if (slot == 14) {
            new PlayerWarps(player, 1);
        }
    }
}
