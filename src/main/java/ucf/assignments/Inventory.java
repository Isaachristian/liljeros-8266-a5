package ucf.assignments;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Inventory {
    private final List<InventoryItem> items;
    private SortBy sortBy;
    private Integer nextId = 0;

    public Inventory() {
        items = new LinkedList<>();
        sortBy = SortBy.NONE;
    }

    public List<InventoryItem> getItems(String filter) {
        List<InventoryItem> filteredSortedItems = new LinkedList<>(items);


        return filteredSortedItems.stream()
                // account for filter
                .filter(item ->
                        item.getSerialNumber().toLowerCase(Locale.ROOT).contains(filter.toLowerCase(Locale.ROOT)) ||
                        item.getName().toLowerCase(Locale.ROOT).contains(filter.toLowerCase(Locale.ROOT)) ||
                        item.getValue().toLowerCase(Locale.ROOT).contains(filter.toLowerCase(Locale.ROOT)) ||
                        item.getIsChanging() // allow changing items to still display
                // account for sortBy order
                ).sorted(new SortByComparer(sortBy)).collect(Collectors.toList());
    }

    public void addItem() {
        InventoryItem inventoryItem = new InventoryItem(nextId++);
        items.add(inventoryItem);
    }

    // for use with importing file
    public void addItem(String serialNumber, String name, double value) {
        InventoryItem inventoryItem = new InventoryItem(nextId++);
        inventoryItem.markAsDirty();
        inventoryItem.setSerialNumber(serialNumber);
        inventoryItem.setName(name);
        inventoryItem.setValue(value);
        items.add(inventoryItem);
    }

    public void deleteItem(Integer id) {
        items.remove(findItemByID(id));
    }

    private InventoryItem findItemByID(Integer id) {
        for (InventoryItem item : items) {
            if (item.getId().equals(id)) {
                return item;
            }
        }

        return null;
    }

    public SortBy getSortBy() {
        return sortBy;
    }

    public void setSortBy(SortBy sortBy) {
        if (this.sortBy == sortBy) {
            this.sortBy = SortBy.NONE;
        } else {
            this.sortBy = sortBy;
        }
    }

    public boolean isUniqueSerialNumber(int id, String serialNumber) {
        for (InventoryItem item : items) {
            if (item.getSerialNumber().equals(serialNumber) && id != item.getId()) {
                return false;
            }
        }
        return true;
    }

    public boolean isItemBeingEdited() {
        for (InventoryItem item : items) {
            if (item.getIsChanging()) {
                return true;
            }
        }
        return false;
    }

    private static class SortByComparer implements Comparator<InventoryItem> {
        SortBy sortBy;

        public SortByComparer(SortBy sortBy) {
            this.sortBy = sortBy;
        }

        @Override
        public int compare(InventoryItem item1, InventoryItem item2) {
            int returnValue = 0;
            switch (sortBy) {
                case SERIALNUMBER -> returnValue = item1.getSerialNumber().compareTo(item2.getSerialNumber());
                case NAME -> returnValue = item1.getName().compareTo(item2.getName());
                case VALUE -> returnValue = Double.compare(item1.getValueAsDouble(), item2.getValueAsDouble());
            }
            return returnValue;
        }
    }
}
