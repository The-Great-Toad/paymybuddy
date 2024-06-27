package openclassroom.p6.paymybuddy.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import openclassroom.p6.paymybuddy.constante.Messages;
import openclassroom.p6.paymybuddy.domain.User;
import openclassroom.p6.paymybuddy.domain.record.UserRequest;
import openclassroom.p6.paymybuddy.service.ContactService;
import openclassroom.p6.paymybuddy.service.TransactionService;
import openclassroom.p6.paymybuddy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final String LOG_ID = "[LoginController]";
    private final UserService userService;
    private final TransactionService transactionService;
    private final ContactService contactService;

    // Home page
    @GetMapping({"/", "index.html"})
    public String home(Model model, Authentication authentication) {
//        User user = (User) authentication.getPrincipal();
        User user = userService.getUser("test@test.com");

        model.addAttribute("user", user);
        model.addAttribute("breadcrumb", "");
        model.addAttribute("recentTransactions", transactionService.getRecentTransactions(user.getEmail()));
        model.addAttribute("recentContacts", contactService.getRecentContacts(user.getEmail()));

        return "index";
    }

    // Login form
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // logout
    @GetMapping("/logout")
    public String login(
            Model model,
            Authentication authentication)
    {
        logger.info("{} - login out user: {}", LOG_ID, authentication.getName());
        model.addAttribute("logout", true);
        return "login";
    }

    // Registration
    @GetMapping("/register")
    public String showRegisterationForm(Model model)
    {
        UserRequest userRequest = new UserRequest("","","","");
        model.addAttribute("userRequest", userRequest);
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(
            Model model,
            @Valid UserRequest userRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes)
    {
        logger.info("{} - userRequest: {}", LOG_ID, userRequest.toString());

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> logger.error("{} - {}", LOG_ID, error));
            return "register";
        }

        User registered = userService.registerNewUser(userRequest);

        if (Objects.isNull(registered)) {
            model.addAttribute("emailUsed", Messages.EMAIL_ALREADY_USED);
            return "register";
        }

        redirectAttributes.addFlashAttribute("registrationSuccess", Messages.REGISTRATION_SUCCESS);
        return "redirect:/login";
    }
}
