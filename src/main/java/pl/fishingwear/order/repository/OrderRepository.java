package pl.fishingwear.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.fishingwear.order.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
