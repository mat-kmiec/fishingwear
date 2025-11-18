package pl.fishingwear.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.fishingwear.admin.dto.EditUserRequest;
import pl.fishingwear.admin.service.AdminService;
import pl.fishingwear.user.model.User;

@Controller
@RequestMapping("/admin/uzytkownicy")
@RequiredArgsConstructor
public class AdminUserController {


    private final AdminService adminService;

    @GetMapping()
    public String users(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(value = "search", required = false) String search,
                        @RequestParam(value = "editSuccess", required = false) String editSuccess,
                        @RequestParam(value = "editError", required = false) String editError,
                        Model model) {

        Page<User> userPage = adminService.getUsers(page, size, search);

        model.addAttribute("users", userPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);

        model.addAttribute("addressSuccess", editSuccess != null);
        model.addAttribute("addressError", editError != null);

        return "admin/manage-users";
    }


    @PostMapping("/update")
    public String updateUser(EditUserRequest request) {
        try{
            adminService.updateUser(request);
            return "redirect:/admin/uzytkownicy?editSuccess";
        }catch (Exception e){
            return "redirect:/admin/uzytkownicy?editError";
        }

    }
}
