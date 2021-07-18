package xyz.nkomarn.Phase.type;

import com.firestartermc.kerosene.item.ItemBuilder;
import com.firestartermc.kerosene.util.AdvancementUtils;
import com.firestartermc.kerosene.util.MessageUtils;
import com.firestartermc.kerosene.util.PlayerUtils;
import com.firestartermc.kerosene.util.TeleportUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.nkomarn.Phase.util.Search;

import java.text.NumberFormat;
import java.util.UUID;
import java.util.stream.Collectors;

public class Warp {

    public boolean visitsModified;
    private String name;
    private UUID owner;
    private Location location;
    private int visits;
    private Category category;
    private boolean featured;
    private boolean expired;
    private long renewed;
    private String description;

    public Warp(@NotNull String name, @NotNull UUID owner, @NotNull Location location, @NotNull Category category, int visits, boolean featured, boolean expired, long renewed, String description) {
        this.name = name;
        this.owner = owner;
        this.location = location;
        this.category = category;
        this.visits = visits;
        this.featured = featured;
        this.expired = expired;
        this.renewed = renewed;
        this.description = description;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public UUID getOwner() {
        return owner;
    }

    public void setOwner(@NotNull UUID owner) {
        this.owner = owner;
    }

    @NotNull
    public Location getLocation() {
        return location;
    }

    public void setLocation(@NotNull Location location) {
        this.location = location;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
        visitsModified = true;
    }

    public void incrementVisits() {
        visits++;
        visitsModified = true;
    }

    @NotNull
    public Category getCategory() {
        return category;
    }

    public void setCategory(@NotNull Category category) {
        this.category = category;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public long getLastRenewed() {
        return renewed;
    }

    public void setLastRenewed(long renewed) {
        this.renewed = renewed;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @NotNull
    public ItemStack getDisplayItem() {
        var builder = ItemBuilder.of(category.getMaterial())
                .name(ChatColor.WHITE + name)
                .lore(MessageUtils.formatColors("&#59ffa1" + NumberFormat.getInstance().format(visits) + " visits", true))
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);

        if (getDescription() != null && getDescription().length() > 0) {
            var description = MessageUtils.splitString(getDescription(), 30).stream()
                    .map(line -> MessageUtils.formatColors(ChatColor.WHITE + line))
                    .collect(Collectors.toList());

            builder.addLore(" ");
            builder.addLore(description);
        }

        return builder.build();
    }

    public void teleport(@NotNull Player player) {
        TeleportUtils.teleportAsync(player, location).thenAccept(success -> {
            if (!success) {
                player.sendTitle(ChatColor.RED + ChatColor.BOLD.toString() + "WARP FAILURE", "An error occurred- notify staff", 10, 70, 20);
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                return;
            }

            player.sendTitle(MessageUtils.formatColors("&#dcff9c&lWHOOSH", true), "You've arrived at '" + name + "'", 10, 40, 10);
            player.playSound(location, Sound.BLOCK_ENDER_CHEST_OPEN, 1.0f, 1.0f);
            player.getWorld().playEffect(location, Effect.DRAGON_BREATH, 3);

            if (!player.getUniqueId().equals(owner)) {
                Search.incrementVisits(this);
            }
        });

        var owner = Bukkit.getPlayer(this.owner);
        if (owner == null) {
            return;
        }

        if (visits >= 1000 && visits < 10000) {
            AdvancementUtils.grant(owner, "warp-visits-1");
        } else if (visits >= 10000) {
            AdvancementUtils.grant(owner, "warp-visits-2");
        }
    }
}
