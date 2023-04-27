package model;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerOrder {
    private Long id;

    private String customerName;

    private String Email;

    private String phonenumber;

    private String product;

    private String address;

    private String quantity;

    private String orderStatus;

    @Override
    public String toString() {
        return "CustomerOrder{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", Email='" + Email + '\'' +
                ", phonenumber=" + phonenumber +
                ", product='" + product + '\'' +
                ", address='" + address + '\'' +
                ", quantity=" + quantity +
                ", orderStatus='" + orderStatus + '\'' +
                '}';
    }
}
