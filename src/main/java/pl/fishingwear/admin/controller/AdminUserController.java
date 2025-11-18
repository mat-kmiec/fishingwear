package pl.fishingwear.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminUserController {

    @GetMapping("/uzytkownicy")
    public String users() {
        return "admin/manage-users";
    }
}
