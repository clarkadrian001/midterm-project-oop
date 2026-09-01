import java.util.ArrayList;
import java.util.Scanner;

public class InventoryManager {
    private ArrayList<Item> items;
    public InventoryManager() {
        items = new ArrayList<>();
    }

    public void addItem(Scanner sc) {
        int bannerWidth = printHeaderBanner("ADD ITEM");
        printCategoryList(bannerWidth);

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
                if (findItemById(id) == null) {
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
            printTable(items, true, "ADDED ITEMS");

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
        printHeaderBanner("UPDATE ITEM");

        try {
            Item item = null;
            boolean validId = false;

            while (!validId) {
                String id = Validator.readValidId(sc, "Enter ID of item to update: ");
                item = findItemById(id);
                if (item != null) {
                    validId = true;
                    break;
                }
                System.out.println("Item not found!");
            }

            printItemDetails(item, "ITEM DETAILS");

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
                System.out.println("Price of Item " + item.getName() + " is updated from " + formatPrice(oldValue) + " to " + formatPrice(newValue));
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
        printHeaderBanner("REMOVE ITEM");

        try {
            Item item = null;
            boolean validId = false;

            while (!validId) {
                String id = Validator.readValidId(sc, "Enter ID of item to remove: ");
                item = findItemById(id);

                if (item != null) {
                    validId = true;
                    break;
                }
                System.out.println("Item not found!");
            }

            items.remove(item);
            System.out.println("Item " + item.getName() + " has been removed from the inventory");
            printTable(items, true);

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

        int bannerWidth = printHeaderBanner("DISPLAY ITEMS BY CATEGORY");

        printCategoryList(bannerWidth);

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

            // The banner and table can naturally differ in width
            // now, since the table is labeled and shown on its own.
            printTable(filtered, false, category.toUpperCase() + " ITEMS");

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

        int itemsWidth = computeItemsBorderLength(items, true);
        printHeaderBanner("DISPLAY ALL ITEMS", itemsWidth);

        printTable(items, true);
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
                item = findItemById(id);
                if (item != null) {
                    validId = true;
                } else {
                    System.out.println("Item not found!");
                }
            }

            printItemDetails(item, "SEARCH RESULT");

        } catch (CancelledException e) {
            System.out.println("Cancelled. Returning to main menu.");
            System.out.println();
        }
    }

    /**
     * Prints a single item's details as a bordered box with a
     * banner above it, e.g.:
     *
     * ================ SEARCH RESULT ================
     * -------------------------------------------------
     * Item ID  : C001
     * Name     : Denim Jacket
     * Category : Clothing
     * Quantity : 10
     * Price    : Php 1,200.50
     * -------------------------------------------------
     *
     * The banner and the box are sized to whichever is
     * naturally wider - the banner's own fixed pattern, or the
     * longest "Label : Value" line - so they always match each
     * other exactly (mutual alignment). "title" lets different
     * screens reuse this with their own wording (e.g. Search
     * Item uses "SEARCH RESULT", Update Item uses "ITEM DETAILS").
     */
    private void printItemDetails(Item item, String title) {
        String[] labels = {"Item ID", "Name", "Category", "Quantity", "Price"};
        String[] values = {item.getId(), item.getName(), item.getCategory(), String.valueOf(item.getQuantity()), formatPrice(item.getPrice())};

        int maxLabelLength = 0;
        for (String label : labels) {
            maxLabelLength = Math.max(maxLabelLength, label.length());
        }

        String[] lines = new String[labels.length];
        int maxLineLength = 0;

        for (int i = 0; i < labels.length; i++) {
            lines[i] = String.format("%-" + maxLabelLength + "s : %s", labels[i], values[i]);
            maxLineLength = Math.max(maxLineLength, lines[i].length());
        }

        String defaultBanner = "================ " + title + " ================";
        int sharedWidth = Math.max(defaultBanner.length(), maxLineLength);

        // Printed directly (not through printHeaderBanner()) so
        // callers control their own spacing around this box.
        System.out.println(buildFilledBanner(title, sharedWidth, '='));

        String detailsBorder = "-".repeat(sharedWidth);
        System.out.println(detailsBorder);

        for (String line : lines) {
            System.out.println(line);
        }
        System.out.println(detailsBorder);
        System.out.println();
    }

    public void sortItems(Scanner sc) {
        if (items.isEmpty()) {
            System.out.println();
            System.out.println("The inventory doesn't have an item to sort. Please add an item first.");
            System.out.println();
            return;
        }
        printHeaderBanner("SORT ITEMS");

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
            printTable(sorted, true, "SORTED ITEMS BY " + sortedByField);

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

        printTable(lowStock, true, "LOW STOCK ITEMS");
    }

    private Item findItemById(String id) {
        for (Item i : items) {
            if (i.getId().equalsIgnoreCase(id)) {
                return i;
            }
        }
        return null;
    }

    private String formatPrice(long scaledPrice) {
        long wholePart = scaledPrice / Validator.PRICE_SCALE;
        long decimalPart = scaledPrice % Validator.PRICE_SCALE;

        int decimalPlaces;
        if (decimalPart % 100 == 0) {
            decimalPlaces = 2;
        } else if (decimalPart % 10 == 0) {
            decimalPlaces = 3;
        } else {
            decimalPlaces = 4;
        }

        String decimalDigits = String.format("%04d", decimalPart).substring(0, decimalPlaces);
        String wholePartFormatted = String.format("%,d", wholePart);
        return "Php " + wholePartFormatted + "." + decimalDigits;
    }

    private String[] getTableHeaders(boolean showCategory) {
        return showCategory
                ? new String[]{"ID", "Name", "Quantity", "Price", "Category"}
                : new String[]{"ID", "Name", "Quantity", "Price"};
    }

    private ArrayList<String[]> buildTableRows(ArrayList<Item> list, boolean showCategory) {
        ArrayList<String[]> rows = new ArrayList<>();

        for (Item i : list) {
            String priceText = formatPrice(i.getPrice());
            if (showCategory) {
                rows.add(new String[]{i.getId(), i.getName(), String.valueOf(i.getQuantity()), priceText, i.getCategory()});
            } else {
                rows.add(new String[]{i.getId(), i.getName(), String.valueOf(i.getQuantity()), priceText});
            }
        }
        return rows;
    }

    private int[] computeColumnWidths(String[] headers, ArrayList<String[]> rows) {
        int columnCount = headers.length;
        int[] columnWidth = new int[columnCount];

        for (int c = 0; c < columnCount; c++) {
            columnWidth[c] = headers[c].length();
        }
        for (String[] row : rows) {
            for (int c = 0; c < columnCount; c++) {
                columnWidth[c] = Math.max(columnWidth[c], row[c].length());
            }
        }
        return columnWidth;
    }

    private int computeItemsBorderLength(ArrayList<Item> list, boolean showCategory) {
        if (list.isEmpty()) {
            return 42;
        }
        String[] headers = getTableHeaders(showCategory);
        ArrayList<String[]> rows = buildTableRows(list, showCategory);
        int[] columnWidth = computeColumnWidths(headers, rows);
        return buildBorderLine(columnWidth).length();
    }

    /**
     * Prints the table with the default "ITEMS" title - used by
     * every screen except Add Item's post-add confirmation.
     */
    private void printTable(ArrayList<Item> list, boolean showCategory) {
        printTable(list, showCategory, "ITEMS");
    }

    /**
     * Same table-printing logic as above, but with a custom
     * title instead of always saying "ITEMS" - e.g. Add Item
     * uses "ADDED ITEMS" here to label the result of adding a
     * new item, without needing a second banner above it.
     */
    private void printTable(ArrayList<Item> list, boolean showCategory, String title) {
        if (list.isEmpty()) {
            System.out.println("No items to display.");
            System.out.println();
            return;
        }

        String[] headers = getTableHeaders(showCategory);
        ArrayList<String[]> rows = buildTableRows(list, showCategory);
        int[] columnWidth = computeColumnWidths(headers, rows);

        String border = buildBorderLine(columnWidth);
        int innerWidth = border.length() - 2;

        String titleBorder = "-".repeat(innerWidth + 2);
        String titleRow = centerText(title, innerWidth + 2);
        System.out.println(titleBorder);
        System.out.println(titleRow);
        System.out.println(border);
        System.out.println(buildRowLine(headers, columnWidth));
        System.out.println(border);

        for (String[] row : rows) {
            System.out.println(buildRowLine(row, columnWidth));
        }
        System.out.println(border);
        System.out.println();
    }

    private String buildBorderLine(int[] columnWidth) {
        StringBuilder sb = new StringBuilder();
        sb.append("-");

        for (int width : columnWidth) {
            sb.append("-".repeat(width + 2)).append("-");
        }
        return sb.toString();
    }

    private String buildRowLine(String[] values, int[] columnWidth) {
        StringBuilder sb = new StringBuilder();
        sb.append(" ");

        for (int c = 0; c < values.length; c++) {
            sb.append(" ").append(String.format("%-" + columnWidth[c] + "s", values[c])).append("  ");
        }
        return sb.toString();
    }

    private void printCategoryList(int width) {
        System.out.println(buildFilledBanner("ITEM CATEGORY LIST", width, '-'));
        
        for (String category : Validator.CATEGORIES) {
            System.out.println(category);
        }
        System.out.println("-".repeat(width));
    }

    private String centerText(String text, int width) {
        if (text.length() >= width) {
            return text.substring(0, width);
        }
        int totalPadding = width - text.length();
        int left = totalPadding / 2;
        int right = totalPadding - left;
        return " ".repeat(left) + text + " ".repeat(right);
    }

    private String buildFilledBanner(String title, int totalWidth, char fillChar) {
        String labelWithSpaces = " " + title + " ";
        if (totalWidth <= labelWithSpaces.length()) {
            return labelWithSpaces;
        }
        int totalPadding = totalWidth - labelWithSpaces.length();
        int left = totalPadding / 2;
        int right = totalPadding - left;
        String fill = String.valueOf(fillChar);
        return fill.repeat(left) + labelWithSpaces + fill.repeat(right);
    }

    private int printHeaderBanner(String title) {
        System.out.println();
        String banner = "================ " + title + " ================";
        System.out.println(banner);
        return banner.length();
    }

    private int printHeaderBanner(String title, int targetWidth) {
        System.out.println();
        String banner = buildFilledBanner(title, targetWidth, '=');
        System.out.println(banner);
        return banner.length();
    }
}