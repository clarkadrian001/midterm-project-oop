import java.util.ArrayList;

public class Design {
    public static String formatPrice(long scaledPrice) {
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

    public static String[] getTableHeaders(boolean showCategory) {
        return showCategory
                ? new String[]{"ID", "Name", "Quantity", "Price", "Category"}
                : new String[]{"ID", "Name", "Quantity", "Price"};
    }

    public static ArrayList<String[]> buildTableRows(ArrayList<Item> list, boolean showCategory) {
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

    public static int[] computeColumnWidths(String[] headers, ArrayList<String[]> rows) {
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

    public static int computeItemsBorderLength(ArrayList<Item> list, boolean showCategory) {
        if (list.isEmpty()) {
            return 42;
        }

        String[] headers = getTableHeaders(showCategory);
        ArrayList<String[]> rows = buildTableRows(list, showCategory);
        int[] columnWidth = computeColumnWidths(headers, rows);

        return buildBorderLine(columnWidth).length();
    }

    public static void printTable(ArrayList<Item> list, boolean showCategory) {
        printTable(list, showCategory, "ITEMS");
    }

    public static void printTable(ArrayList<Item> list, boolean showCategory, String title) {
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

    public static String buildBorderLine(int[] columnWidth) {
        StringBuilder sb = new StringBuilder();
        sb.append("-");

        for (int width : columnWidth) {
            sb.append("-".repeat(width + 2)).append("-");
        }

        return sb.toString();
    }

    public static String buildRowLine(String[] values, int[] columnWidth) {
        StringBuilder sb = new StringBuilder();
        sb.append(" ");

        for (int c = 0; c < values.length; c++) {
            sb.append(" ").append(String.format("%-" + columnWidth[c] + "s", values[c])).append("  ");
        }

        return sb.toString();
    }

    public static void printCategoryList(int width) {
        System.out.println(buildFilledBanner("ITEM CATEGORY LIST", width, '-'));

        for (String category : Validator.CATEGORIES) {
            System.out.println(category);
        }

        System.out.println("-".repeat(width));
    }

    public static String centerText(String text, int width) {
        if (text.length() >= width) {
            return text.substring(0, width);
        }

        int totalPadding = width - text.length();
        int left = totalPadding / 2;
        int right = totalPadding - left;

        return " ".repeat(left) + text + " ".repeat(right);
    }

    public static String buildFilledBanner(String title, int totalWidth, char fillChar) {
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

    public static int printHeaderBanner(String title) {
        System.out.println();
        String banner = "================ " + title + " ================";
        System.out.println(banner);

        return banner.length();
    }

    public static int printHeaderBanner(String title, int targetWidth) {
        System.out.println();
        String banner = buildFilledBanner(title, targetWidth, '=');
        System.out.println(banner);
        
        return banner.length();
    }

    public static void printItemDetails(Item item, String title) {
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

        System.out.println(buildFilledBanner(title, sharedWidth, '='));

        String detailsBorder = "-".repeat(sharedWidth);
        System.out.println(detailsBorder);

        for (String line : lines) {
            System.out.println(line);
        }
        
        System.out.println(detailsBorder);
        System.out.println();
    }
}