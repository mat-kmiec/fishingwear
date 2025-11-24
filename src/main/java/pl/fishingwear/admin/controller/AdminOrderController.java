package pl.fishingwear.admin.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.fishingwear.admin.dto.order.OrderAdminListDto;
import pl.fishingwear.admin.service.OrderManagementService;
import pl.fishingwear.order.model.OrderStatus;
import pl.fishingwear.order.service.OrderService;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class AdminOrderController {


    private final OrderManagementService orderManagementService;

    public AdminOrderController(OrderManagementService orderManagementService) {
        this.orderManagementService = orderManagementService;
    }

    @GetMapping("/zamowienia")
    public String listOrders(
            @RequestParam(required = false) String searchQuery,
            @RequestParam(required = false) OrderStatus statusFilter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFilter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<OrderAdminListDto> orderPage = orderManagementService.getOrdersForAdmin(searchQuery, statusFilter, dateFilter, pageable);
        model.addAttribute("ordersPage", orderPage);
        model.addAttribute("allStatuses", OrderStatus.values());
        model.addAttribute("currentSearch", searchQuery);
        model.addAttribute("currentStatus", statusFilter);
        model.addAttribute("currentDate", dateFilter);
        return "admin/manage-orders";
    }

    @GetMapping("/zamowienia/szczegoly/{id}")
    public String orderDetails(@PathVariable Long id, Model model) {
        return "admin/order";
    }
}
