import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        InventoryManager manager = new InventoryManager();
        boolean running = true;

        System.out.println("=======================================");
        System.out.println("      INVENTORY MANAGEMENT SYSTEM      ");
        System.out.println("=======================================");

        while (running) {
            System.out.println("Menu");
            System.out.println("1 - Add Item");
            System.out.println("2 - Update Item");
            System.out.println("3 - Remove Item");
            System.out.println("4 - Display Items by Category");
            System.out.println("5 - Display All Items");
            System.out.println("6 - Search Item");
            System.out.println("7 - Sort Items");
            System.out.println("8 - Display Low Stock Items");
            System.out.println("9 - Exit");
            System.out.println();
            System.out.println("Note: Press 0 to cancel anytime.");

            int choice = Validator.readMenuChoice(sc, "Enter choice: ", 1, 9);

            switch (choice) {
                case 1:
                    manager.addItem(sc);
                    break;
                case 2:
                    manager.updateItem(sc);
                    break;
                case 3:
                    manager.removeItem(sc);
                    break;
                case 4:
                    manager.displayItemsByCategory(sc);
                    break;
                case 5:
                    manager.displayAllItems();
                    break;
                case 6:
                    manager.searchItem(sc);
                    break;
                case 7:
                    manager.sortItems(sc);
                    break;
                case 8:
                    manager.displayLowStockItems();
                    break;
                case 9:
                    running = false;
                    System.out.println();
                    System.out.println("================ EXIT ================");
                    System.out.println("Exiting program. Goodbye!");
                    break;
            }
        }

        sc.close();
    }
}