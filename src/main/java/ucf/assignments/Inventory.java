package ucf.assignments;

import java.util.LinkedList;
import java.util.List;

public class Inventory {
    private final List<InventoryItem> items;
    private SortBy sortBy;
    private Integer nextId = 0;

    public Inventory() {
        items = new LinkedList<>();
        sortBy = SortBy.NONE;
    }

    public List<InventoryItem> getItems(String filter) {

        return items;
    }

    public void addItem() {
        InventoryItem inventoryItem = new InventoryItem(nextId++);
        items.add(inventoryItem);
    }

    public void addItem(String serialNumber, String name, double value) {
        InventoryItem inventoryItem = new InventoryItem(nextId++);
        inventoryItem.markAsDirty();
        inventoryItem.setSerialNumber(serialNumber);
        inventoryItem.setName(name);
        inventoryItem.setValue(value);
        items.add(inventoryItem);
    }

    public void deleteItem(Integer id) {

    }

    private InventoryItem findItemByID(Integer id) {

        return null;
    }

    public SortBy getSortBy() {
        return sortBy;
    }

    public void setSortBy(SortBy sortBy) {
        this.sortBy = sortBy;
    }

}
