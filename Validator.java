import java.util.Scanner;

public class Validator {
    public static final String[] CATEGORIES = {"Clothing", "Electronics", "Entertainment"};
    public static final int MAX_ID_LENGTH = 20;
    public static final long PRICE_SCALE = 10_000L;
    public static final int MAX_NAME_LENGTH = 50;
    public static final int MAX_QUANTITY = 1_000_000;
    public static final long MAX_PRICE = 10_000_000L;
    private static void checkForCancel(String input) {
        if (input.equals("0")) {
            throw new CancelledException();
        }
    }

    private static boolean isSingleDigit(String rawInput) {
        return rawInput.length() == 1 && Character.isDigit(rawInput.charAt(0));
    }

    public static boolean isValidCategory(String category) {
        for (String c : CATEGORIES) {
            if (c.equalsIgnoreCase(category)) {
                return true;
            }
        }
        return false;
    }

    public static int readMenuChoice(Scanner sc, String prompt, int min, int max) {
        int choice = -1;
        boolean valid = false;

        while (!valid) {
            System.out.print(prompt);
            String input = sc.nextLine();

            if (!isSingleDigit(input)) {
                System.out.println("Invalid input! Please enter a single digit with no spaces or extra characters.");
                continue;
            }

            choice = Character.getNumericValue(input.charAt(0));
            if (choice >= min && choice <= max) {
                valid = true;
            } else {
                System.out.println("Please enter a number between " + min + " and " + max + ".");
            }
        }
        return choice;
    }

    public static int readSubMenuChoice(Scanner sc, String prompt, int min, int max) {
        int choice = -1;
        boolean valid = false;

        while (!valid) {
            System.out.print(prompt);
            String input = sc.nextLine();
            checkForCancel(input);

            if (!isSingleDigit(input)) {
                System.out.println("Invalid input! Please enter a single digit with no spaces or extra characters.");
                continue;
            }

            choice = Character.getNumericValue(input.charAt(0));
            if (choice >= min && choice <= max) {
                valid = true;
            } else {
                System.out.println("Please enter a number between " + min + " and " + max + ".");
            }
        }
        return choice;
    }

    public static String toTitleCase(String text) {
        String[] words = text.split(" ");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                String firstLetter = word.substring(0, 1).toUpperCase();
                String restOfWord = word.substring(1).toLowerCase();
                result.append(firstLetter).append(restOfWord);
            }
            result.append(" ");
        }

        return result.toString().trim();
    }

    public static String readNonEmptyString(Scanner sc, String prompt) {
        String value;
        while (true) {
            System.out.print(prompt);
            value = sc.nextLine().trim();
            checkForCancel(value);
            if (value.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
            } else if (value.length() > MAX_NAME_LENGTH) {
                System.out.println("Name is too long! Maximum of " + MAX_NAME_LENGTH + " characters allowed.");
            } else {
                return value;
            }
        }
    }

    public static boolean isValidId(String id) {
        return !id.isEmpty()
                && id.length() <= MAX_ID_LENGTH
                && id.matches("[a-zA-Z0-9]+");
    }

    public static String readValidId(Scanner sc, String prompt) {
        String value;
        while (true) {
            System.out.print(prompt);
            value = sc.nextLine().trim();
            checkForCancel(value);

            if (value.isEmpty()) {
                System.out.println("ID cannot be empty. Please try again.");
            } else if (value.length() > MAX_ID_LENGTH) {
                System.out.println("ID is too long! Maximum of " + MAX_ID_LENGTH + " characters allowed.");
            } else if (!value.matches("[a-zA-Z0-9]+")) {
                System.out.println("Invalid ID! Only letters and numbers are allowed (no spaces or symbols).");
            } else {
                return value;
            }
        }
    }

    public static int readPositiveInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            checkForCancel(input);
            try {
                int value = Integer.parseInt(input);
                if (value < 0) {
                    System.out.println("Quantity cannot be negative. Please try again.");
                } else if (value > MAX_QUANTITY) {
                    System.out.println("Quantity is too high! Maximum of " + MAX_QUANTITY + " allowed.");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a whole number.");
            }
        }
    }

    public static long readPositivePrice(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            checkForCancel(input);
            try {
                double value = Double.parseDouble(input);
                if (value < 0) {
                    System.out.println("Price cannot be negative. Please try again.");
                } else if (value > MAX_PRICE) {
                    System.out.println("Price is too high! Maximum of " + MAX_PRICE + " allowed.");
                } else {
                    return Math.round(value * PRICE_SCALE);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number (e.g. 199.99).");
            }
        }
    }
}