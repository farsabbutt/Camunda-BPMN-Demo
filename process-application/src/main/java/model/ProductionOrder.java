package model;

public class ProductionOrder {
    private String customerName;
    private int productionOrderId;
    private String customerEmail;
    private String productName;
    private int orderedQuantity;
    private String estimatedProductionTime;
    private String productToBeManufactured;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getProductionOrderId() {
        return productionOrderId;
    }

    public void setProductionOrderId(int productionOrderId) {
        this.productionOrderId = productionOrderId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getOrderedQuantity() {
        return orderedQuantity;
    }

    public void setOrderedQuantity(int orderedQuantity) {
        this.orderedQuantity = orderedQuantity;
    }

    public String getEstimatedProductionTime() {
        return estimatedProductionTime;
    }

    public void setEstimatedProductionTime(String estimatedProductionTime) {
        this.estimatedProductionTime = estimatedProductionTime;
    }

    public String getProductToBeManufactured() {
        return productToBeManufactured;
    }

    public void setProductToBeManufactured(String productToBeManufactured) {
        this.productToBeManufactured = productToBeManufactured;
    }

    @Override
    public String toString() {
        return "ProductionOrder{" +
                "customerName='" + customerName + '\'' +
                ", customerOrderId=" + productionOrderId +
                ", customerEmail='" + customerEmail + '\'' +
                ", productName='" + productName + '\'' +
                ", orderedQuantity=" + orderedQuantity +
                ", estimatedProductionTime='" + estimatedProductionTime + '\'' +
                ", productToBeManufactured='" + productToBeManufactured + '\'' +
                '}';
    }


}
