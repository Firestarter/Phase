package xyz.nkomarn.Phase.gui.menu;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.Phase.gui.GuiHolder;
import xyz.nkomarn.Phase.gui.GuiType;
import xyz.nkomarn.Phase.gui.inventory.CategoryPicker;
import xyz.nkomarn.Phase.gui.inventory.PlayerWarps;
import xyz.nkomarn.Phase.type.Warp;
import xyz.nkomarn.Phase.util.Config;
import xyz.nkomarn.Phase.util.Search;
import xyz.nkomarn.Phase.util.WarpUtil;
import xyz.nkomarn.campfire.util.Claims;
import xyz.nkomarn.kerosene.gui.Gui;
import xyz.nkomarn.kerosene.gui.components.buttons.ButtonComponent;
import xyz.nkomarn.kerosene.gui.components.cosmetic.BorderAlternatingComponent;
import xyz.nkomarn.kerosene.gui.components.cosmetic.FillComponent;
import xyz.nkomarn.kerosene.gui.predefined.ConfirmationGui;
import xyz.nkomarn.kerosene.util.item.ItemBuilder;

import java.util.Arrays;
import java.util.Optional;

public class SettingsMenu extends Gui {

    public SettingsMenu(@NotNull Warp warp) {
        super("Settings: " + warp.getName(), 3);

        addElement(new FillComponent(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").build()));
        addElement(new BorderAlternatingComponent(
                new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name(" ").build(),
                new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").build()
        ));

        ItemStack back = new ItemBuilder(Material.PAPER, 1)
                .name("&b&lBack")
                .build();
        addElement(new ButtonComponent(0, 1, back, event -> new PlayerWarps(event.getPlayer(), 1)));

        ItemStack filler = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1)
                .name(" ")
                .build();
        addElement(new ButtonComponent(1, 1, filler, event -> {}));

        ItemStack teleport = new ItemBuilder(Material.ENDER_EYE, 1)
                .name("&f&lTeleport")
                .lore("&7Teleport to the warp's", "&7current location.")
                .build();
        addElement(new ButtonComponent(2, 1, teleport, event -> WarpUtil.warpPlayer(event.getPlayer(), warp)));

        ItemStack rename = new ItemBuilder(Material.NAME_TAG, 1)
                .name("&f&lRename")
                .lore("&7Change your warp's name.")
                .build();
        addElement(new ButtonComponent(3, 1, rename, event -> rename(event.getPlayer(), warp)));

        ItemStack relocate = new ItemBuilder(Material.FILLED_MAP, 1)
                .name("&f&lRelocate")
                .lore("&7Move the warp to your", "&7current location.")
                .build();
        addElement(new ButtonComponent(4, 1, relocate, event -> relocate(event.getPlayer(), warp)));

        ItemStack category = new ItemBuilder(Material.HOPPER, 1)
                .name("&f&lChange Category")
                .lore("&7Make your warp more", "&7visible to players.")
                .build();
        addElement(new ButtonComponent(5, 1, category, event -> changeCategory(event.getPlayer(), warp)));

        ItemStack delete = new ItemBuilder(Material.BARRIER, 1)
                .name("&c&lDelete Warp")
                .lore("&7Completely remove your", "&7warp from existence.")
                .build();
        addElement(new ButtonComponent(7, 1, delete, event -> delete(event.getPlayer(), warp)));
    }

    private void rename(@NotNull Player player, @NotNull Warp warp) {
        new AnvilGUI.Builder()
                .onComplete((p, text) -> {
                    Optional<Warp> existingWarp = Search.getWarpByName(text);

                    if (existingWarp.isPresent()) {
                        p.sendMessage(Config.getPrefix() + "A warp by the name '" + text + "' already exists.");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                        return AnvilGUI.Response.close();
                    }

                    Bukkit.getScheduler().runTaskAsynchronously(Phase.getPhase(), () -> // TODO once futures, remove async task
                            WarpUtil.renameWarp(warp.getName(), ChatColor.stripColor(text)));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, 1.0f);
                    player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6&lRenamed warp."), "It's now called '" + text + "'.", 10, 70, 20);

                    return AnvilGUI.Response.close();
                })
                .title("Rename your Warp")
                .text("Enter a new name.")
                .item(new ItemStack(Material.BOOK))
                .plugin(Phase.getPhase())
                .open(player);
    }

    private void relocate(@NotNull Player player, @NotNull Warp warp) {
        if (Claims.checkForeignClaims(player, player.getLocation())) {
            player.sendMessage(Config.getPrefix() + "You can't move warps to others' claims.");
            return;
        }

        WarpUtil.relocateWarp(warp.getName(), player.getLocation());
        player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6&lRelocated"), "The warp is now at your location.", 10, 70, 20);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, 1.0f);
    }

    private void changeCategory(@NotNull Player player, @NotNull Warp warp) {
        new CategoryPicker(player, warp);
    }

    private void delete(@NotNull Player player, @NotNull Warp warp) {
        new ConfirmationGui("Delete warp '" + warp.getName() + "'.",
                (event) -> {
                    event.getGui().close();
                    WarpUtil.delete(warp.getName());
                    player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&c&lDeleted"), "The warp was blown away with the wind.", 10, 70, 20);
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                },
                (event) -> event.getGui().close()
        ).open(player);
    }
}