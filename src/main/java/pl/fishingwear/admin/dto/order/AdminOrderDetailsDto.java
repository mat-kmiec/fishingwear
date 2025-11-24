package pl.fishingwear.admin.dto.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.fishingwear.order.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class AdminOrderDetailsDto {
    private Long id;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String shippingAddress;
    private String zipCode;
    private String city;
    private BigDecimal subtotal;
    private BigDecimal shippingCost;
    private BigDecimal total;
    private String shippingMethod;
    private List<AdminOrderItemDto> items;
    public String getFullAddress() {
        return String.format("%s, %s %s", shippingAddress, zipCode, city);
    }
    public String getFullName() {
        return firstName + " " + lastName;
    }
}