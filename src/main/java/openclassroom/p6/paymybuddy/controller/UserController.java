package openclassroom.p6.paymybuddy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final String LOG_ID = "[UserController]";


    @GetMapping
    public String profileView(Model model) {
        return "profile";
    }
}
