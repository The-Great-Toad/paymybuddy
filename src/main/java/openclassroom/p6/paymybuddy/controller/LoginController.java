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

    /* HOME */

    @GetMapping({"/", "index.html"})
    public String getHome(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
//        User user = userService.getUser("test@test.com");

        model.addAttribute("user", user);
        model.addAttribute("breadcrumb", "Home");
        model.addAttribute("recentTransactions", transactionService.getRecentTransactions(user.getEmail()));
        model.addAttribute("recentContacts", contactService.getRecentContacts(user.getEmail()));

        return "index";
    }

    /* LOGIN & LOGOUT */

    @GetMapping("/login")
    public String getLoginForm() {
        return "login";
    }

    @PostMapping("/user/logout")
    public String logout(RedirectAttributes redirectAttributes, Authentication authentication)
    {
        logger.info("{} - login out user: {}", LOG_ID, authentication.getName());
        userService.logoutUser();
        redirectAttributes.addFlashAttribute("logoutSuccess", Messages.LOGOUT_SUCCESS);
        return "redirect:/login";
    }

    /* REGISTRATION */

    @GetMapping("/register")
    public String getRegistrationForm(Model model)
    {
        UserRequest userRequest = new UserRequest("","","","");
        model.addAttribute("userRequest", userRequest);
        return "register";
    }

    @PostMapping("/register/save")
    public String register(
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
