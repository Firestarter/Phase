package xyz.nkomarn.Phase.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GUIHolder implements InventoryHolder {
    private GUIType type;
    private int page;
    private String data;

    public GUIHolder(final GUIType type, final int page) {
        this.type = type;
        this.page = page;
    }

    public GUIHolder(final GUIType type, final int page,
                     final String data) {
        this.type = type;
        this.page = page;
        this.data = data;
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

    public String getData() {
        return this.data;
    }
}
