public abstract class Sale implements Chargeable {
    private final String id;
    private String customerName;
    private int customerAge;

    protected Sale(String id, String customerName, int customerAge)
            throws InvalidSaleException {
        if (id == null || id.isBlank()) {
            throw new InvalidSaleException("ID cannot be blank.");
        }
        if (customerName == null || customerName.isBlank()) {
            throw new InvalidSaleException("Customer name cannot be blank.");
        }
        if (customerAge < 0 || customerAge > 120) {
            throw new InvalidSaleException("Age must be between 0 and 120.");
        }
        this.id = id;
        this.customerName = customerName.trim();
        this.customerAge = customerAge;
    }
        public String getId() {
        return id;
    }
    public String getCustomerName() {
        return customerName;
    }
    public int getCustomerAge() {
        return customerAge;
    }
    public void setCustomerName(String customerName)
            throws InvalidSaleException {
        if (customerName == null || customerName.isBlank()) {
            throw new InvalidSaleException("Customer name cannot be blank.");
        }
        this.customerName = customerName.trim();
    }
    public void setCustomerAge(int customerAge)
            throws InvalidSaleException {
        if (customerAge < 0 || customerAge > 120) {
            throw new InvalidSaleException("Age must be between 0 and 120.");
        }
        this.customerAge = customerAge;
    }
    public boolean receivesDiscount() {
        return customerAge >= 60;
    }
    public final double calculateDiscount() {
        if (receivesDiscount()) {
            return calculateSubtotal() * 0.12;
        }
        return 0.0;
    }
    public abstract double calculateSubtotal();

    public abstract String getSaleType();

    @Override
    public abstract double calculateCharge();
}

