package pl.fishingwear.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.fishingwear.common.exception.UserNotFoundException;
import pl.fishingwear.order.service.OrderService;
import pl.fishingwear.theme.dto.ThemeDto;
import pl.fishingwear.theme.service.ThemeService;
import pl.fishingwear.user.model.Address;
import pl.fishingwear.user.model.User;
import pl.fishingwear.user.service.AddressService;
import pl.fishingwear.user.service.UserService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class UserController {


    private final UserService userService;
    private final AddressService addressService;
    private final OrderService orderService;
    private final ThemeService themeService;

    public UserController(UserService userService, AddressService addressService, OrderService orderService, ThemeService themeService) {
        this.userService = userService;
        this.addressService = addressService;
        this.orderService = orderService;
        this.themeService = themeService;
    }

    @GetMapping("/moje-konto")
    public String user(Model model) {
        int userCount = userService.countAllUsers();
        model.addAttribute("userCount", userCount);
        return "user/review";
    }

    @GetMapping("/historia-zamowien")
    public String orderHistory(Model model,  Principal principal) {
        if (principal == null) {
            return "redirect:/logowanie";
        }
        model.addAttribute("orders", orderService.findOrdersForUser(principal));

        return "user/orders-list";
    }



    @GetMapping("/ustawienia-profilu")
    public String userSettings(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        List<ThemeDto> themes = themeService.getAllThemes();
        model.addAttribute("currentUser", user);
        model.addAttribute("themes", themes);

        return "user/user-settings";
    }


    @GetMapping("/adresy")
    public String addresses(Model model, Principal principal,
                            @RequestParam(value = "addressSuccess", required = false) String addressSuccess,
                            @RequestParam(value = "addressError", required = false) String addressError,
                            @RequestParam(value = "deleted", required = false) String deleted,
                            @RequestParam(value = "unauthorized", required = false) String unauthorized) {
        if (principal == null) {
            return "redirect:/logowanie";
        }
        User user;
        try {
            user = userService.findByEmail(principal.getName());
        }catch (UserNotFoundException e){
            return "redirect:/logowanie";
        }
        model.addAttribute("user", user);
        model.addAttribute("addresses", user.getAddresses());
        model.addAttribute("addressSuccess", addressSuccess != null);
        model.addAttribute("addressError", addressError != null);
        model.addAttribute("deleted", deleted != null);
        model.addAttribute("unauthorized", unauthorized != null);
        return "user/address";
    }



    @PostMapping("/user/update")
    public String updateUser(@ModelAttribute("user") User updatedUser, Principal principal) {
        if (principal == null) {
            return "redirect:/logowanie";
        }

        try {
            userService.updateUserData(principal.getName(), updatedUser);
            return "redirect:/adresy?userSuccess";
        } catch (Exception e) {
            return "redirect:/adresy?userError";
        }
    }

    @PostMapping("/adresy/add")
    public String addAddress(@ModelAttribute Address address, Principal principal) {
        if (principal == null) {
            return "redirect:/logowanie";
        }

        User user = userService.findByEmail(principal.getName());
        address.setUser(user);
        address.setCreatedAt(LocalDateTime.now());
        addressService.save(address);

        return "redirect:/adresy?addressSuccess";
    }

    @GetMapping("/adresy/delete/{id}")
    public String deleteAddress(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/logowanie";
        }

        try {
            User user = userService.findByEmail(principal.getName());
            addressService.deleteByIdAndUser(id, user);
            return "redirect:/adresy?deleted";
        } catch (SecurityException e) {
            return "redirect:/adresy?unauthorized";
        } catch (Exception e) {
            return "redirect:/adresy?addressError";
        }
    }


}
