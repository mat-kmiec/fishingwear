package pl.fishingwear.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {


    @GetMapping("/moje-konto")
    public String user() {
        return "user/review";
    }

    @GetMapping("/historia-zamowien")
    public String orderHistory() {
        return "user/orders-list";
    }

    @GetMapping("/adresy")
    public String addresses() {
        return "user/address";
    }

    @GetMapping("/ustawienia-profilu")
    public String profileSettings() {
        return "user/user-settings";
    }

}
