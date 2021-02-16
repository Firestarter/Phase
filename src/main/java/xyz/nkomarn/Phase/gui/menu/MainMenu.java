package xyz.nkomarn.Phase.gui.menu;

import com.firestartermc.kerosene.gui.Gui;
import com.firestartermc.kerosene.gui.components.buttons.ButtonComponent;
import com.firestartermc.kerosene.gui.components.cosmetic.BorderAlternatingComponent;
import com.firestartermc.kerosene.item.ItemBuilder;
import com.firestartermc.kerosene.item.SkullBuilder;
import com.firestartermc.kerosene.util.MessageUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import xyz.nkomarn.Phase.gui.inventory.FeaturedWarps;
import xyz.nkomarn.Phase.gui.inventory.FilteredWarps;
import xyz.nkomarn.Phase.gui.inventory.PlayerWarps;
import xyz.nkomarn.Phase.gui.inventory.PublicWarps;
import xyz.nkomarn.Phase.type.Category;

public class MainMenu extends Gui {

    public MainMenu() {
        super(MessageUtils.formatColors("&lWARPS&r Menu", false), 6);

        addElement(new BorderAlternatingComponent(Material.MAGENTA_STAINED_GLASS_PANE, Material.PURPLE_STAINED_GLASS_PANE));

        var featured = ItemBuilder.of(Material.NETHER_STAR)
                .name("&d&lFEATURED WARPS")
                .lore(
                        "&7View a hand-picked selection",
                        "&7of high-quality warps.",
                        " ",
                        "&7Visit " + ChatColor.of("#ff8aeb") + "#warp-feature &7on Discord",
                        "&7to apply for feature."
                )
                .build();
        addElement(new ButtonComponent(4, 0, featured, event -> new FeaturedWarps(event.getPlayer(), 1)));

        var community = ItemBuilder.of(Category.ALL.getMaterial())
                .name(ChatColor.of("#ff8aeb") + ChatColor.BOLD.toString() + "ALL WARPS")
                .lore("&7View all of the public", "&7warps on the server.")
                .build();
        addElement(new ButtonComponent(2, 2, community, event -> new PublicWarps(event.getPlayer(), 1)));

        var shop = ItemBuilder.of(Category.SHOP.getMaterial())
                .name("&e&lSHOPS")
                .build();
        addElement(new ButtonComponent(3, 2, shop, event -> new FilteredWarps(event.getPlayer(), 1, Category.SHOP.getName())));

        var utility = ItemBuilder.of(Category.UTILITY.getMaterial())
                .name("&b&lUTILITY")
                .build();
        addElement(new ButtonComponent(4, 2, utility, event -> new FilteredWarps(event.getPlayer(), 1, Category.UTILITY.getName())));

        var farm = ItemBuilder.of(Category.FARM.getMaterial())
                .name("&6&lFARMS")
                .build();
        addElement(new ButtonComponent(5, 2, farm, event -> new FilteredWarps(event.getPlayer(), 1, Category.FARM.getName())));

        var town = ItemBuilder.of(Category.TOWN.getMaterial())
                .name("&a&lTOWNS")
                .build();
        addElement(new ButtonComponent(6, 2, town, event -> new FilteredWarps(event.getPlayer(), 1, Category.TOWN.getName())));

        var attraction = ItemBuilder.of(Category.ATTRACTION.getMaterial())
                .name("&c&lATTRACTIONS")
                .build();
        addElement(new ButtonComponent(3, 3, attraction, event -> new FilteredWarps(event.getPlayer(), 1, Category.ATTRACTION.getName())));

        var pvp = ItemBuilder.of(Category.PVP.getMaterial())
                .name("&e&lPvP")
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .build();
        addElement(new ButtonComponent(4, 3, pvp, event -> new FilteredWarps(event.getPlayer(), 1, Category.PVP.getName())));

        var grinder = ItemBuilder.of(Category.GRINDER.getMaterial())
                .name("&a&lGRINDERS")
                .build();
        addElement(new ButtonComponent(5, 3, grinder, event -> new FilteredWarps(event.getPlayer(), 1, Category.GRINDER.getName())));
    }

    @Override
    public void onOpen(Player player) {
        var skull = new SkullBuilder()
                .name(ChatColor.of("#8af5ff") + ChatColor.BOLD.toString() + "YOUR WARPS")
                .lore("&7Create and view", "&7your own warps.")
                .player(player)
                .build();
        addElement(new ButtonComponent(4, 5, skull, event -> new PlayerWarps(event.getPlayer(), 1)));
    }
}
