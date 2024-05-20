package openclassroom.p6.paymybuddy.controller;

import openclassroom.p6.paymybuddy.domain.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contact")
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);
    private final String LOG_ID = "[ContactController]";

    @GetMapping
    public String saveContact(Model model, @Validated @RequestBody Contact contact) {
        return "contact";
    }
}
