package me.kamikid.blog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexAboutController {
    @GetMapping("/about")
    public String about(){
        return "about";
    }
}
