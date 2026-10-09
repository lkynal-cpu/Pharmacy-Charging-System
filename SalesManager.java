import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class SalesManager {
    private final ArrayList<Sale> sales = new ArrayList<>();
    private int nextNumber = 1;
    private final String idPrefix;

    public SalesManager(String initials, String registrationSuffix) {
        idPrefix = initials.toUpperCase() + registrationSuffix;
    }

    public String nextId() {
        return String.format("%s-%03d", idPrefix, nextNumber++);
    }

    public void addSale(Sale sale) throws InvalidSaleException {
        if (findById(sale.getId()).isPresent()) {
            throw new InvalidSaleException("Sale ID already exists.");
        }
        sales.add(sale);
    }

    public Optional<Sale> findById(String id) {
        return sales.stream()
                .filter(sale -> sale.getId().equalsIgnoreCase(id))
                .findFirst();
    }

    public boolean removeById(String id) {
        return sales.removeIf(
                sale -> sale.getId().equalsIgnoreCase(id));
    }

    public List<Sale> getSortedSales() {
        ArrayList<Sale> sorted = new ArrayList<>(sales);
        sorted.sort(Comparator.comparing(
                Sale::getCustomerName,
                String.CASE_INSENSITIVE_ORDER));
        return sorted;
    }

    public double getTotalCharges() {
        return sales.stream()
                .mapToDouble(Sale::calculateCharge)
                .sum();
    }

    public double getTotalDiscount() {
        return sales.stream()
                .mapToDouble(Sale::calculateDiscount)
                .sum();
    }

    public int getSaleCount() {
        return sales.size();
    }
}
