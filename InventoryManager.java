import java.util.ArrayList;
import java.util.Scanner;

public class InventoryManager {
    private ArrayList<Item> items;
    public InventoryManager() {
        items = new ArrayList<>();
    }

    public void addItem(Scanner sc) {
        int bannerWidth = Design.printHeaderBanner("ADD ITEM");
        Design.printCategoryList(bannerWidth);

        try {
            String category = null;
            boolean validCategory = false;

            while (!validCategory) {
                System.out.print("Enter Category: ");
                category = sc.nextLine().trim();

                if (category.equals("0")) {
                    throw new CancelledException();
                }

                if (Validator.isValidCategory(category)) {
                    validCategory = true;
                    break;
                }
                System.out.println("Category " + category + " does not exist!");
            }

            String id = null;
            boolean validId = false;

            while (!validId) {
                id = Validator.readValidId(sc, "Enter ID: ");
                id = id.toUpperCase();

                if (Validator.findItemById(items, id) == null) {
                    validId = true;
                    break;
                }

                System.out.println("An item with ID " + id + " already exists!");
            }

            String name = Validator.readNonEmptyString(sc, "Enter Name: ");
            name = Validator.toTitleCase(name);

            int quantity = Validator.readPositiveInt(sc, "Enter Quantity: ");
            long price = Validator.readPositivePrice(sc, "Enter Price: ");

            Item newItem = createItem(category, id, name, quantity, price);
            items.add(newItem);

            System.out.println("Item added successfully!");
            Design.printTable(items, true, "ADDED ITEMS");

        } catch (CancelledException e) {
            System.out.println("Cancelled. Returning to main menu.");
            System.out.println();
        }
    }

    private Item createItem(String category, String id, String name, int quantity, long price) {
        switch (category.toLowerCase()) {
            case "clothing":
                return new Clothing(id, name, quantity, price);
            case "electronics":
                return new Electronics(id, name, quantity, price);
            default:
                return new Entertainment(id, name, quantity, price);
        }
    }

    public void updateItem(Scanner sc) {
        if (items.isEmpty()) {
            System.out.println();
            System.out.println("The inventory doesn't have an item to update. Please add an item first.");
            System.out.println();

            return;
        }

        Design.printHeaderBanner("UPDATE ITEM");

        try {
            Item item = null;
            boolean validId = false;

            while (!validId) {
                String id = Validator.readValidId(sc, "Enter ID of item to update: ");
                item = Validator.findItemById(items, id);

                if (item != null) {
                    validId = true;
                    break;
                }

                System.out.println("Item not found!");
            }

            Design.printItemDetails(item, "ITEM DETAILS");

            System.out.println("1 - Quantity");
            System.out.println("2 - Price");
            int fieldChoice = Validator.readSubMenuChoice(sc, "Enter choice: ", 1, 2);

            if (fieldChoice == 1) {
                int oldValue = item.getQuantity();
                int newValue = Validator.readPositiveInt(sc, "Enter new Quantity: ");

                item.setQuantity(newValue);

                System.out.println("Quantity of Item " + item.getName() + " is updated from " + oldValue + " to " + newValue);
                System.out.println();
            } else {
                long oldValue = item.getPrice();
                long newValue = Validator.readPositivePrice(sc, "Enter new Price: ");

                item.setPrice(newValue);

                System.out.println("Price of Item " + item.getName() + " is updated from " + Design.formatPrice(oldValue) + " to " + Design.formatPrice(newValue));
                System.out.println();
            }

        } catch (CancelledException e) {
            System.out.println("Cancelled. Returning to main menu.");
            System.out.println();
        }
    }

    public void removeItem(Scanner sc) {
        if (items.isEmpty()) {
            System.out.println();
            System.out.println("The inventory doesn't have an item to remove. Please add an item first.");
            System.out.println();

            return;
        }

        Design.printHeaderBanner("REMOVE ITEM");

        try {
            Item item = null;
            boolean validId = false;

            while (!validId) {
                String id = Validator.readValidId(sc, "Enter ID of item to remove: ");
                item = Validator.findItemById(items, id);

                if (item != null) {
                    validId = true;
                    break;
                }

                System.out.println("Item not found!");
            }

            items.remove(item);
            System.out.println("Item " + item.getName() + " has been removed from the inventory");
            Design.printTable(items, true);

        } catch (CancelledException e) {
            System.out.println("Cancelled. Returning to main menu.");
            System.out.println();
        }
    }

    public void displayItemsByCategory(Scanner sc) {
        if (items.isEmpty()) {
            System.out.println();
            System.out.println("The inventory doesn't have an item to display by category. Please add an item first.");
            System.out.println();

            return;
        }

        int bannerWidth = Design.printHeaderBanner("DISPLAY ITEMS BY CATEGORY");

        Design.printCategoryList(bannerWidth);

        try {
            String category = null;
            boolean validCategory = false;

            while (!validCategory) {
                System.out.print("Enter Category: ");
                category = sc.nextLine().trim();

                if (category.equals("0")) {
                    throw new CancelledException();
                }

                if (Validator.isValidCategory(category)) {
                    validCategory = true;
                } else {
                    System.out.println("Category " + category + " does not exist!");
                }
            }

            ArrayList<Item> filtered = new ArrayList<>();
            for (Item i : items) {
                if (i.getCategory().equalsIgnoreCase(category)) {
                    filtered.add(i);
                }
            }

            Design.printTable(filtered, false, category.toUpperCase() + " ITEMS");

        } catch (CancelledException e) {
            System.out.println("Cancelled. Returning to main menu.");
            System.out.println();
        }
    }

    public void displayAllItems() {
        if (items.isEmpty()) {
            System.out.println();
            System.out.println("The inventory doesn't have an item to display. Please add an item first.");
            System.out.println();

            return;
        }

        int itemsWidth = Design.computeItemsBorderLength(items, true);
        Design.printHeaderBanner("DISPLAY ALL ITEMS", itemsWidth);

        Design.printTable(items, true);
    }

    public void searchItem(Scanner sc) {
        if (items.isEmpty()) {
            System.out.println();
            System.out.println("The inventory doesn't have an item to search. Please add an item first.");
            System.out.println();

            return;
        }

        try {
            Item item = null;
            boolean validId = false;

            while (!validId) {
                String id = Validator.readValidId(sc, "Enter ID to search: ");
                item = Validator.findItemById(items, id);
                
                if (item != null) {
                    validId = true;
                } else {
                    System.out.println("Item not found!");
                }
            }

            Design.printItemDetails(item, "SEARCH RESULT");

        } catch (CancelledException e) {
            System.out.println("Cancelled. Returning to main menu.");
            System.out.println();
        }
    }

    public void sortItems(Scanner sc) {
        if (items.isEmpty()) {
            System.out.println();
            System.out.println("The inventory doesn't have an item to sort. Please add an item first.");
            System.out.println();

            return;
        }
        Design.printHeaderBanner("SORT ITEMS");

        try {
            System.out.println("1 - Quantity");
            System.out.println("2 - Price");
            int fieldChoice = Validator.readSubMenuChoice(sc, "Enter choice: ", 1, 2);

            System.out.println("1 - Ascending");
            System.out.println("2 - Descending");
            int orderChoice = Validator.readSubMenuChoice(sc, "Enter choice: ", 1, 2);

            ArrayList<Item> sorted = new ArrayList<>(items);

            for (int i = 0; i < sorted.size() - 1; i++) {
                for (int j = 0; j < sorted.size() - 1 - i; j++) {
                    Item a = sorted.get(j);
                    Item b = sorted.get(j + 1);

                    long valueA = (fieldChoice == 1) ? a.getQuantity() : a.getPrice();
                    long valueB = (fieldChoice == 1) ? b.getQuantity() : b.getPrice();

                    boolean shouldSwap = (orderChoice == 1) ? (valueA > valueB) : (valueA < valueB);

                    if (shouldSwap) {
                        sorted.set(j, b);
                        sorted.set(j + 1, a);
                    }
                }
            }

            String sortedByField = (fieldChoice == 1) ? "QUANTITY" : "PRICE";
            Design.printTable(sorted, true, "SORTED ITEMS BY " + sortedByField);

        } catch (CancelledException e) {
            System.out.println("Cancelled. Returning to main menu.");
            System.out.println();
        }
    }

    public void displayLowStockItems() {
        if (items.isEmpty()) {
            System.out.println();
            System.out.println("The inventory doesn't have an item. Please add and item first.");
            System.out.println();
            
            return;
        }

        ArrayList<Item> lowStock = new ArrayList<>();
        for (Item i : items) {
            if (i.getQuantity() <= 5) {
                lowStock.add(i);
            }
        }

        Design.printTable(lowStock, true, "LOW STOCK ITEMS");
    }
}