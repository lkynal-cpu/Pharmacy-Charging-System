import java.util.Scanner;
import java.util.List;
public class Main {
    private final InputHelper input;
    private final SalesManager manager;

    private Main(Scanner scanner, SalesManager manager) {
        input = new InputHelper(scanner);
        this.manager = manager;
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            InputHelper setup = new InputHelper(scanner);

            String fullName = setup.readText("Enter your full name: ");
            if (fullName == null) {
                return;
            }

            String registration = setup.readText(
                    "Enter your registration number ending in 075: ");
            if (registration == null) {
                return;
            }

            String digits = registration.replaceAll("\\D", "");
            while (!digits.endsWith("075")) {
                System.out.println("For this scenario, the number must end in 075.");
                registration = setup.readText(
                        "Enter your registration number ending in 075: ");
                if (registration == null) {
                    return;
                }
                digits = registration.replaceAll("\\D", "");
            }

            String initials = getInitials(fullName);
            String suffix = digits.substring(digits.length() - 3);
            SalesManager manager = new SalesManager(initials, suffix);

            System.out.println("\nPharmacy Charging System");
            System.out.println("Student: " + fullName
                    + " | Registration: " + registration);
            System.out.println("Discount: 12% for customers aged 60 or over");

            Main program = new Main(scanner, manager);
            program.runMenu();
        }
    }

    private static String getInitials(String name) {
        StringBuilder initials = new StringBuilder();
        String[] words = name.trim().split("\\s+");

        for (String word : words) {
            initials.append(Character.toUpperCase(word.charAt(0)));
        }

        return initials.toString();
    }

    private void runMenu() {
        while (true) {
            System.out.println("\n1. Add prescription sale");
            System.out.println("2. Add counter sale");
            System.out.println("3. List sales");
            System.out.println("4. Find a sale");
            System.out.println("5. Remove a sale");
            System.out.println("6. Show summary");
            System.out.println("0. Exit");

            Integer choice = input.readInt("Choose an option: ", 0, 6);

            if (choice == null || choice == 0) {
                System.out.println("Goodbye.");
                return;
            }

            switch (choice) {
                case 1:
                    addSale(false);
                    break;
                case 2:
                    addSale(true);
                    break;
                case 3:
                    showReport();
                    break;
                case 4:
                    findSale();
                    break;
                case 5:
                    removeSale();
                    break;
                case 6:
                    showSummary();
                    break;
                default:
                    System.out.println("Choose one of the listed options.");
            }
        }
    }

    private void showSummary() {
        System.out.printf(
                "Sales: %d | Total charges: UGX %,.2f | Total discount: UGX %,.2f%n",
                manager.getSaleCount(),
                manager.getTotalCharges(),
                manager.getTotalDiscount());
    }
    private void addSale(boolean counterSale) {
        String name = input.readText("Customer name: ");
        if (name == null) {
            return;
        }

        Integer age = input.readInt("Customer age (0-120): ", 0, 120);
        if (age == null) {
            return;
        }

        Integer quantity = input.readInt(
                "Quantity (minimum 1): ", 1, Integer.MAX_VALUE);
        if (quantity == null) {
            return;
        }

        Double price = input.readAmount("Price per item (UGX): ");
        if (price == null) {
            return;
        }

        String id = manager.nextId();

        try {
            Sale sale;

            if (counterSale) {
                sale = new CounterSale(id, name, age, quantity, price);
            } else {
                sale = new PrescriptionSale(id, name, age, quantity, price);
            }

            manager.addSale(sale);

            System.out.printf(
                    "Added %s with ID %s. Charge: UGX %,.2f "
                            + "(discount: UGX %,.2f)%n",
                    sale.getSaleType(),
                    sale.getId(),
                    sale.calculateCharge(),
                    sale.calculateDiscount());
        } catch (InvalidSaleException exception) {
            System.out.println("Sale was not added: "
                    + exception.getMessage());
        }
    }
        private void showReport() {
        List<Sale> sortedSales = manager.getSortedSales();

        if (sortedSales.isEmpty()) {
            System.out.println("No sales to display.");
            return;
        }

        System.out.println(
                "\nID | Customer | Age | Sale type | Charge (UGX) | Discount (UGX)");

        for (Sale sale : sortedSales) {
            System.out.printf(
                    "%s | %s | %d | %s | %,.2f | %,.2f%n",
                    sale.getId(),
                    sale.getCustomerName(),
                    sale.getCustomerAge(),
                    sale.getSaleType(),
                    sale.calculateCharge(),
                    sale.calculateDiscount());
        }
    }

    private void findSale() {
        String id = input.readText("Sale ID: ");
        if (id == null) {
            return;
        }

        Sale sale = manager.findById(id).orElse(null);
        if (sale == null) {
            System.out.println("No sale found with that ID.");
            return;
        }

        System.out.printf("Found %s for %s. Charge: UGX %,.2f%n",
                sale.getId(), sale.getCustomerName(), sale.calculateCharge());
    }

    private void removeSale() {
        String id = input.readText("Sale ID to remove: ");
        if (id == null) {
            return;
        }

        if (manager.findById(id).isEmpty()) {
            System.out.println("No sale found with that ID.");
            return;
        }

        String answer = input.readText("Confirm removal? (yes/no): ");
        if (answer == null) {
            return;
        }

        if (answer.equalsIgnoreCase("yes")) {
            if (manager.removeById(id)) {
                System.out.println("Sale removed.");
            } else {
                System.out.println("Sale could not be removed.");
            }
        } else {
            System.out.println("Removal cancelled.");
        }
    }
}
