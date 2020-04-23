package xyz.nkomarn.Phase.type;

import org.bukkit.Material;

public enum Category {
    ALL("All", Material.BOOK),
    SHOP("Shop", Material.CHEST),
    UTILITY("Utility", Material.ENDER_CHEST),
    GRINDER("Grinder", Material.EXPERIENCE_BOTTLE),
    PVP("PvP", Material.GOLDEN_SWORD),
    ATTRACTION("Attraction", Material.FIREWORK_ROCKET),
    TOWN("Town", Material.LIGHT_BLUE_BED);

    private final String name;
    private final Material material;

    Category(final String name, final Material material) {
        this.name = name;
        this.material = material;
    }

    public String getName() {
        return this.name;
    }

    public Material getMaterial() {
        return this.material;
    }
}
