import java.util.Scanner;

public class InputHelper {
    private final Scanner scanner;

    public InputHelper(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return null;
            }

            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("Please enter a value; it cannot be blank.");
        }
    }

    public Integer readInt(String prompt, int minimum, int maximum) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return null;
            }

            String value = scanner.nextLine().trim();
            try {
                int number = Integer.parseInt(value);
                if (number >= minimum && number <= maximum) {
                    return number;
                }
            } catch (NumberFormatException exception) {
                System.out.println("That is not a whole number.");
                continue;
            }

            System.out.println("Enter a whole number from "
                    + minimum + " to " + maximum + ".");
        }
    }

    public Double readAmount(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return null;
            }

            String value = scanner.nextLine().trim();
            try {
                double amount = Double.parseDouble(value);
                if (Double.isFinite(amount) && amount >= 0) {
                    return amount;
                }
            } catch (NumberFormatException exception) {
                System.out.println("That is not a valid amount.");
                continue;
            }

            System.out.println("Enter an amount that is zero or more.");
        }
    }
}
