public class PrescriptionSale extends Sale {
    private int quantity;
    private double unitPrice;

    public PrescriptionSale(String id, String customerName, int customerAge,
            int quantity, double unitPrice)
            throws InvalidSaleException {
        super(id, customerName, customerAge);
        setQuantity(quantity);
        setUnitPrice(unitPrice);
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setQuantity(int quantity)
            throws InvalidSaleException {
        if (quantity <= 0) {
            throw new InvalidSaleException("Quantity must be greater than zero.");
        }
        this.quantity = quantity;
    }

    public void setUnitPrice(double unitPrice)
            throws InvalidSaleException {
        if (!Double.isFinite(unitPrice) || unitPrice < 0) {
            throw new InvalidSaleException("Price must be zero or more.");
        }
        this.unitPrice = unitPrice;
    }

    public double calculateSubtotal() {
        return quantity * unitPrice;
    }

    public String getSaleType() {
        return "PrescriptionSale";
    }

    public double calculateCharge() {
        return calculateSubtotal() - calculateDiscount();
    }
}