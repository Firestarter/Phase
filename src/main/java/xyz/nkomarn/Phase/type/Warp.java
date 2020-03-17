package xyz.nkomarn.Phase.type;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.nkomarn.Phase.util.WarpUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Warp implements Comparable<Warp> {
    private DecimalFormat formatter = new DecimalFormat("#,###");
    private String name, owner, category, world;
    private AtomicInteger visits;
    private long renewed;
    private double x, y, z, pitch, yaw;
    private boolean featured, expired;

    // TODO remove type- only public warps from now on
    public Warp(final String name, final String owner, final int visits, final String category,
                final boolean featured, final boolean expired, final long renewed, final double x, final double y,
                final double z, final double pitch, final double yaw, final String world) {
        this.name = name;
        this.owner = owner;
        this.visits = new AtomicInteger(visits);
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

    public void setName(final String name) {
        this.name = name;
    }

    public UUID getOwnerUUID() {
        return UUID.fromString(owner);
    }

    public AtomicInteger getVisits() {
        return this.visits;
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

    public void setLocation(final Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
        this.world = location.getWorld().getUID().toString();
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    public void setExpired(final boolean expired) {
        this.expired = expired;
    }

    public void setRenewed(long timestamp) {
        this.renewed = timestamp;
    }

    public ItemStack getItem(final Player player) {
        ItemStack warpItem = new ItemStack(WarpUtil.getItem(this.getCategory()));
        ItemMeta warpItemMeta = warpItem.getItemMeta();
        warpItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', String.format("&6&l%s", this.getName())));
        warpItemMeta.setLore(Arrays.asList(
                ChatColor.translateAlternateColorCodes('&', String.format("&7Category: &6%s", this.getCategory())),
                ChatColor.translateAlternateColorCodes('&', String.format("&7Visits: &6%s", formatter.format(this.getVisits())))
        ));
        warpItem.setItemMeta(warpItemMeta);
        return warpItem;
    }

    @Override
    public int compareTo(Warp warp) {
        return Integer.compare(this.visits.get(), warp.visits.get());
    }
}
