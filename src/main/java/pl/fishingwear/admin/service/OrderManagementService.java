package pl.fishingwear.admin.service;

import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.fishingwear.admin.dto.order.OrderAdminListDto;
import pl.fishingwear.order.model.Order;
import pl.fishingwear.order.model.OrderStatus;
import pl.fishingwear.order.repository.OrderRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderManagementService {

    private final OrderRepository orderRepository;

    public Page<OrderAdminListDto> getOrdersForAdmin(String searchQuery, OrderStatus statusFilter, LocalDate dateFrom, Pageable pageable) {

        Specification<Order> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(searchQuery)) {
                String likeTerm = "%" + searchQuery.toLowerCase() + "%";

                Predicate emailPredicate = cb.like(cb.lower(root.get("user").get("email")), likeTerm);
                Predicate lastNamePredicate = cb.like(cb.lower(root.get("user").get("lastName")), likeTerm);
                Predicate idPredicate = cb.like(root.get("id").as(String.class), likeTerm);
                predicates.add(cb.or(emailPredicate, lastNamePredicate, idPredicate));
            }

            if (statusFilter != null) {
                predicates.add(cb.equal(root.get("status"), statusFilter));
            }

            if (dateFrom != null) {
                predicates.add(cb.between(
                        root.get("createdAt"),
                        dateFrom.atStartOfDay(),
                        dateFrom.plusDays(1).atStartOfDay()
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Order> ordersPage = orderRepository.findAll(spec, pageable);
        return ordersPage.map(this::mapToDto);
    }

    private OrderAdminListDto mapToDto(Order order) {
        String firstName = order.getUser().getFirstName() != null ? order.getUser().getFirstName() : "";
        String lastName = order.getUser().getLastName() != null ? order.getUser().getLastName() : "";

        return new OrderAdminListDto(
                order.getId(),
                (firstName + " " + lastName).trim(),
                order.getUser().getEmail(),
                order.getCreatedAt(),
                order.getStatus(),
                order.getTotal()
        );
    }
}
