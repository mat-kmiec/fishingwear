package pl.fishingwear.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminSliderController {
    @GetMapping("/slider")
    public String slider() {
        return "admin/slider";
    }

    @GetMapping("/slider/edycja")
    public String sliderEdit() {
        return "admin/slider-edit";
    }
}
