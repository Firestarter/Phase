package xyz.nkomarn.Phase.type;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.nkomarn.Phase.util.WarpUtil;

import java.util.Arrays;
import java.util.UUID;

public class Warp {
    private final String name, owner, world;
    private final Category category;
    private final int visits;
    private final long renewed;
    private final double x, y, z, pitch, yaw;
    private final boolean featured, expired;

    public Warp(String name, String owner, int visits, Category category, boolean featured, boolean expired, long renewed,
                double x, double y, double z, double pitch, double yaw, String world) {
        this.name = name;
        this.owner = owner;
        this.visits = visits;
        this.category = category;
        this.featured = featured;
        this.expired = expired;
        this.renewed = renewed;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.world = world;
    }

    public String getName() {
        return this.name;
    }

    public UUID getOwnerUUID() {
        return UUID.fromString(owner);
    }

    public int getVisits() {
        return this.visits;
    }

    public Category getCategory() {
        return this.category;
    }

    public boolean isFeatured() {
        return this.featured;
    }

    public boolean isExpired() {
        return this.expired;
    }

    public long getRenewedTime() {
        return this.renewed;
    }

    public Location getLocation() {
        World world = Bukkit.getWorld(UUID.fromString(this.world));
        return new Location(world, this.x, this.y, this.z, (float) this.yaw, (float) this.pitch);
    }

    public ItemStack getItemStack() {
        ItemStack warpItem = new ItemStack(this.category.getMaterial());
        ItemMeta warpItemMeta = warpItem.getItemMeta();
        warpItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                String.format("&f&l%s", this.name)));
        warpItemMeta.setLore(Arrays.asList(
                ChatColor.translateAlternateColorCodes('&', String.format("&7Category: &a%s", this.category.getName())),
                ChatColor.translateAlternateColorCodes('&', String.format("&7Visits: &b%s",
                        WarpUtil.formatNumber(this.visits)))
        ));
        warpItem.setItemMeta(warpItemMeta);
        return warpItem;
    }
}
