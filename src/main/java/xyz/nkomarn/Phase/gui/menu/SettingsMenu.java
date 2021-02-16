package xyz.nkomarn.Phase.gui.menu;

import com.firestartermc.kerosene.gui.Gui;
import com.firestartermc.kerosene.gui.components.buttons.ButtonComponent;
import com.firestartermc.kerosene.gui.components.cosmetic.BorderAlternatingComponent;
import com.firestartermc.kerosene.gui.components.cosmetic.FillComponent;
import com.firestartermc.kerosene.gui.components.item.ItemComponent;
import com.firestartermc.kerosene.gui.predefined.ConfirmationGui;
import com.firestartermc.kerosene.item.ItemBuilder;
import com.firestartermc.kerosene.util.MessageUtils;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.Phase.Phase;
import xyz.nkomarn.Phase.gui.inventory.CategoryPicker;
import xyz.nkomarn.Phase.gui.inventory.PlayerWarps;
import xyz.nkomarn.Phase.type.Warp;
import xyz.nkomarn.Phase.util.ClaimUtils;
import xyz.nkomarn.Phase.util.Config;
import xyz.nkomarn.Phase.util.Search;
import xyz.nkomarn.Phase.util.WarpUtil;

import java.util.Optional;

public class SettingsMenu extends Gui {

    public SettingsMenu(@NotNull Warp warp) {
        super(MessageUtils.formatColors("&lSETTINGS&r " + warp.getName(), false), 3);

        addElement(new FillComponent(Material.GRAY_STAINED_GLASS_PANE));
        addElement(new BorderAlternatingComponent(Material.BLACK_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS_PANE));

        ItemStack back = ItemBuilder.of(Material.PAPER)
                .name("&b&lBack")
                .build();
        addElement(new ButtonComponent(0, 1, back, event -> new PlayerWarps(event.getPlayer(), 1)));

        ItemStack filler = ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE)
                .name(" ")
                .build();
        addElement(new ItemComponent(1, 1, filler));

        ItemStack teleport = ItemBuilder.of(Material.ENDER_EYE)
                .name("&f&lTeleport")
                .lore("&7Teleport to the warp's", "&7current location.")
                .build();
        addElement(new ButtonComponent(2, 1, teleport, event -> WarpUtil.warpPlayer(event.getPlayer(), warp)));

        ItemStack rename = ItemBuilder.of(Material.NAME_TAG)
                .name("&f&lRename")
                .lore("&7Change your warp's name.")
                .build();
        addElement(new ButtonComponent(3, 1, rename, event -> rename(event.getPlayer(), warp)));

        ItemStack description = ItemBuilder.of(Material.WRITABLE_BOOK)
                .name("&f&lChange Description")
                .lore("&7Change your warp's description.")
                .build();
        addElement(new ButtonComponent(4, 1, description, event -> changeDescription(event.getPlayer(), warp)));

        ItemStack relocate = ItemBuilder.of(Material.FILLED_MAP)
                .name("&f&lRelocate")
                .lore("&7Move the warp to your", "&7current location.")
                .build();
        addElement(new ButtonComponent(5, 1, relocate, event -> relocate(event.getPlayer(), warp)));

        ItemStack category = ItemBuilder.of(Material.HOPPER)
                .name("&f&lChange Category")
                .lore("&7Make your warp more", "&7visible to players.")
                .build();
        addElement(new ButtonComponent(6, 1, category, event -> changeCategory(event.getPlayer(), warp)));

        ItemStack delete = ItemBuilder.of(Material.BARRIER)
                .name("&c&lDelete Warp")
                .lore("&7Completely remove your", "&7warp from existence.")
                .build();
        addElement(new ButtonComponent(7, 1, delete, event -> delete(event.getPlayer(), warp)));
    }

    private void changeDescription(@NotNull Player player, @NotNull Warp warp) {
        new AnvilGUI.Builder()
                .onComplete((p, text) -> {
                    if (text.length() > 50) {
                        return AnvilGUI.Response.text("Description must be 30 characters or less.");
                    }

                    var sanitized = ChatColor.stripColor(MessageUtils.formatColors(text, true)).trim();

                    Bukkit.getScheduler().runTaskAsynchronously(Phase.getPhase(), () -> {
                        WarpUtil.changeWarpDescription(warp.getName(), sanitized);
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, 1.0f);
                        p.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6&lChanged description."), "How exciting.", 10, 70, 20);
                    });

                    return AnvilGUI.Response.close();
                })
                .title("Change warp description")
                .text("Enter a new description.")
                .item(new ItemStack(Material.BOOK))
                .plugin(Phase.getPhase())
                .open(player);
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
        if (ClaimUtils.isForeignClaim(player, player.getLocation())) {
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