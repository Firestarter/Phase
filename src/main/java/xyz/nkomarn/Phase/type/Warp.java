package xyz.nkomarn.Phase.type;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

public class Warp {
    private final String name, owner, category, world;
    private final int visits;
    private final long renewed;
    private final double x, y, z;
    private final float pitch, yaw;
    private final boolean type, featured, expired;
    private final String[] favorites;

    public Warp(final String name, final String owner, final int visits, final boolean type, final String category,
                final boolean featured, final boolean expired, final long renewed, final double x, final double y,
                final double z, final float pitch, final float yaw, final String world, final String[] favorites) {
        this.name = name;
        this.owner = owner;
        this.visits = visits;
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

    public int getVisits() {
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
        return new Location(world, this.x, this.y, this.z, this.yaw, this.pitch);
    }

    public String[] getFavorites() {
        return this.favorites;
    }
}
