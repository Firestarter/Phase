package xyz.nkomarn.Phase.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GuiHolder implements InventoryHolder {
    private final GuiType type;
    private final int page;
    private String data;

    public GuiHolder(GuiType type, int page) {
        this.type = type;
        this.page = page;
    }

    public GuiHolder(GuiType type, int page, String data) {
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

    public GuiType getType() {
        return this.type;
    }

    public String getData() {
        return this.data;
    }
}
