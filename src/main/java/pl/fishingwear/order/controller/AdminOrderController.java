package pl.fishingwear.order.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.fishingwear.order.dto.AdminOrderDetailsDto;
import pl.fishingwear.order.dto.OrderAdminListDto;
import pl.fishingwear.order.service.OrderManagementService;
import pl.fishingwear.order.model.OrderStatus;

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
    public String getOrderDetails(@PathVariable Long id, Model model) {
        try {
            AdminOrderDetailsDto orderDto = orderManagementService.getOrderDetails(id);
            model.addAttribute("order", orderDto);
            model.addAttribute("allStatuses", OrderStatus.values());
            return "admin/order";

        } catch (IllegalArgumentException e) {
            return "redirect:/admin/zamowienia";
        }
    }

    @PostMapping("/zamowienia/{id}/status")
    public String updateOrderStatus(@PathVariable Long id,
                                    @RequestParam("status") OrderStatus newStatus,
                                    RedirectAttributes redirectAttributes) {
        try {
            orderManagementService.updateOrderStatus(id, newStatus);

            redirectAttributes.addFlashAttribute("message", "Status zamówienia został pomyślnie zmieniony na: " + newStatus);
            redirectAttributes.addFlashAttribute("messageType", "success");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Wystąpił błąd: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "danger");
        }

        return "redirect:/admin/zamowienia/szczegoly/" + id;
    }
}
