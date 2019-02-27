package me.kamikid.blog.web.admin;

import me.kamikid.blog.entity.User;
import me.kamikid.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public String loginPage(){
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attribute){
        User user = userService.checkUser(username, password);
        if(user!=null){
            user.setPassword(null);
            session.setAttribute("user", user);
            return "admin/index";
        }else{
            attribute.addFlashAttribute("message", "Invalid Username or Password");
            return "redirect:/admin";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session,
                         RedirectAttributes attribute){
        session.removeAttribute("user");
        attribute.addFlashAttribute("message", "You have logged out...");
        return "redirect:/admin";
    }
}
