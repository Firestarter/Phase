package xyz.nkomarn.Phase.gui.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.nkomarn.Phase.gui.GuiHolder;
import xyz.nkomarn.Phase.gui.GuiType;
import xyz.nkomarn.Phase.gui.inventory.FeaturedWarps;
import xyz.nkomarn.Phase.gui.inventory.FilteredWarps;
import xyz.nkomarn.Phase.gui.inventory.PlayerWarps;
import xyz.nkomarn.Phase.gui.inventory.PublicWarps;
import xyz.nkomarn.Phase.type.Category;
import xyz.nkomarn.kerosene.gui.Gui;
import xyz.nkomarn.kerosene.gui.components.buttons.ButtonComponent;
import xyz.nkomarn.kerosene.gui.components.cosmetic.BorderAlternatingComponent;
import xyz.nkomarn.kerosene.gui.components.cosmetic.FillComponent;
import xyz.nkomarn.kerosene.util.item.ItemBuilder;
import xyz.nkomarn.kerosene.util.item.SkullBuilder;

import java.util.Arrays;

public class MainMenu extends Gui {

    public MainMenu() {
        super("Warps Menu", 6);

       // addElement(new FillComponent(Material.GRAY_STAINED_GLASS_PANE));
        addElement(new BorderAlternatingComponent(
                new ItemBuilder(Material.MAGENTA_STAINED_GLASS_PANE).name(" ").build(),
                new ItemBuilder(Material.PURPLE_STAINED_GLASS_PANE).name(" ").build()
        ));

        ItemStack featured = new ItemBuilder(Material.NETHER_STAR)
                .name("&d&lFEATURED WARPS")
                .lore(
                        "&7View a hand-picked selection",
                        "&7of high-quality warps.",
                        " ",
                        "&7Visit &d#warp-feature &7on Discord",
                        "&7to apply for feature."
                )
                .build();
        addElement(new ButtonComponent(4, 0, featured, event -> new FeaturedWarps(event.getPlayer(), 1)));

        ItemStack community = new ItemBuilder(Category.ALL.getMaterial())
                .name("&f&lALL WARPS")
                .lore("&7View all of the public", "&7warps on the server.")
                .build();
        addElement(new ButtonComponent(2, 2, community, event -> new PublicWarps(event.getPlayer(), 1)));

        ItemStack shop = new ItemBuilder(Category.SHOP.getMaterial())
                .name("&e&lSHOPS")
                .build();
        addElement(new ButtonComponent(3, 2, shop, event -> new FilteredWarps(event.getPlayer(), 1, Category.SHOP.getName())));

        ItemStack utility = new ItemBuilder(Category.UTILITY.getMaterial())
                .name("&b&lUTILITY")
                .build();
        addElement(new ButtonComponent(4, 2, utility, event -> new FilteredWarps(event.getPlayer(), 1, Category.UTILITY.getName())));

        ItemStack farm = new ItemBuilder(Category.FARM.getMaterial())
                .name("&6&lFARMS")
                .build();
        addElement(new ButtonComponent(5, 2, farm, event -> new FilteredWarps(event.getPlayer(), 1, Category.FARM.getName())));

        ItemStack town = new ItemBuilder(Category.TOWN.getMaterial())
                .name("&a&lTOWNS")
                .build();
        addElement(new ButtonComponent(6, 2, town, event -> new FilteredWarps(event.getPlayer(), 1, Category.TOWN.getName())));

        ItemStack attraction = new ItemBuilder(Category.ATTRACTION.getMaterial())
                .name("&c&lATTRACTIONS")
                .build();
        addElement(new ButtonComponent(3, 3, attraction, event -> new FilteredWarps(event.getPlayer(), 1, Category.ATTRACTION.getName())));

        ItemStack pvp = new ItemBuilder(Category.PVP.getMaterial())
                .name("&e&lPvP")
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .build();
        addElement(new ButtonComponent(4, 3, pvp, event -> new FilteredWarps(event.getPlayer(), 1, Category.PVP.getName())));

        ItemStack grinder = new ItemBuilder(Category.GRINDER.getMaterial())
                .name("&a&lGRINDERS")
                .build();
        addElement(new ButtonComponent(5, 3, grinder, event -> new FilteredWarps(event.getPlayer(), 1, Category.GRINDER.getName())));
    }

    @Override
    public void onOpen(Player player) {
        ItemStack skull = new SkullBuilder()
                .name("&b&lYOUR WARPS")
                .lore("&7Create and view", "&7your own warps.")
                .player(player)
                .build();
        addElement(new ButtonComponent(4, 5, skull, event -> new PlayerWarps(event.getPlayer(), 1)));
    }

}
