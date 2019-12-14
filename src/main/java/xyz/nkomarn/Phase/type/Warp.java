package xyz.nkomarn.Phase.type;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.nkomarn.Phase.util.WarpUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Warp {
    private final String name, owner, category, world;
    private AtomicInteger visits;
    private final long renewed;
    private final double x, y, z, pitch, yaw;
    private final boolean type, featured, expired;
    private final ArrayList<String> favorites;

    public Warp(final String name, final String owner, final int visits, final boolean type, final String category,
                final boolean featured, final boolean expired, final long renewed, final double x, final double y,
                final double z, final double pitch, final double yaw, final String world, final ArrayList<String> favorites) {
        this.name = name;
        this.owner = owner;
        this.visits = new AtomicInteger(visits);
        this.type = type;
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
        this.favorites = favorites;
    }

    public String getName() {
        return this.name;
    }

    public UUID getOwnerUUID() {
        return UUID.fromString(owner);
    }

    public AtomicInteger getVisits() {
        return this.visits;
    }

    public boolean getType() {
        return this.type;
    }

    public String getCategory() {
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
        final World world = Bukkit.getWorld(UUID.fromString(this.world));
        return new Location(world, this.x, this.y, this.z, (float) this.yaw, (float) this.pitch);
    }

    public ArrayList<String> getFavorites() {
        return this.favorites;
    }

    public ItemStack getItem() {
        ItemStack warpItem = new ItemStack(WarpUtil.getItem(this.getCategory()));
        ItemMeta warpItemMeta = warpItem.getItemMeta();
        warpItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                String.format("&6&l%s", this.getName())));
        warpItemMeta.setLore(Arrays.asList(
                ChatColor.translateAlternateColorCodes('&', String.format("&7Category: &6%s", this.getCategory())),
                ChatColor.translateAlternateColorCodes('&', String.format("&7Visits: &6%s", this.getVisits()))
        ));
        warpItem.setItemMeta(warpItemMeta);
        return warpItem;
    }
}
