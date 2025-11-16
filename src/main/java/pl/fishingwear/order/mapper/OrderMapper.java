package pl.fishingwear.order.mapper;

import org.springframework.stereotype.Component;
import pl.fishingwear.order.dto.OrderDto;
import pl.fishingwear.order.model.Order;

@Component
public class OrderMapper {
    public OrderDto toDto(Order order){
        return  new OrderDto(
                order.getId(),
                order.getTotal(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }
}
