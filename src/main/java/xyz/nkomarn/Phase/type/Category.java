package xyz.nkomarn.Phase.type;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

/**
 * Enum representing a warp category.
 */
public enum Category {

    ALL("All", Material.BOOK),
    SHOP("Shop", Material.CHEST),
    UTILITY("Utility", Material.ENDER_CHEST),
    FARM("Farm", Material.WHEAT),
    GRINDER("Grinder", Material.EXPERIENCE_BOTTLE),
    PVP("PvP", Material.GOLDEN_SWORD),
    ATTRACTION("Attraction", Material.FIREWORK_ROCKET),
    TOWN("Town", Material.LIME_BED);

    private final String name;
    private final Material material;

    Category(@NotNull String name, @NotNull Material material) {
        this.name = name;
        this.material = material;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @NotNull
    public Material getMaterial() {
        return this.material;
    }

}
