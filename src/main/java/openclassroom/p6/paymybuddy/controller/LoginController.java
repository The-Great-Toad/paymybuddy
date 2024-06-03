package openclassroom.p6.paymybuddy.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final String LOG_ID = "[LoginController]";


    @GetMapping({"/", "index.html"})
    public String home(Model model) {
        model.addAttribute("uri", "home");
        return "index";
    }

}
