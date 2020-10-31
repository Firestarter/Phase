package xyz.nkomarn.Phase.util;

import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ClaimUtils {

    public static boolean isForeignClaim(Player context, Location location) {
        var chunk = location.getChunk();
        var claims = GriefPrevention.instance.dataStore.getClaims(chunk.getX(), chunk.getZ());

        if (claims.isEmpty()) {
            return false;
        }

        return claims.stream()
                .filter(claim -> !claim.ownerID.equals(context.getUniqueId()))
                .anyMatch(claim -> claim.allowBuild(context, Material.AIR) != null);
    }
}
