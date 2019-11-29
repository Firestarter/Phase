package xyz.nkomarn.Phase.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GUIHolder implements InventoryHolder {
    private GUIType type;
    private int page;
    private String category;

    public GUIHolder(final GUIType type, final int page) {
        this.type = type;
        this.page = page;
    }

    public GUIHolder(final GUIType type, final int page,
                     final String category) {
        this.type = type;
        this.page = page;
        this.category = category;
    }

    public Inventory getInventory() {
        return null; // Unused
    }

    public int getPage() {
        return this.page;
    }

    public GUIType getType() {
        return this.type;
    }

    public String getCategory() {
        return this.category;
    }
}
