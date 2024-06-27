package openclassroom.p6.paymybuddy.controller;

import jakarta.validation.Valid;
import openclassroom.p6.paymybuddy.constante.Messages;
import openclassroom.p6.paymybuddy.domain.User;
import openclassroom.p6.paymybuddy.domain.record.AmountRequest;
import openclassroom.p6.paymybuddy.domain.record.UserInfoRequest;
import openclassroom.p6.paymybuddy.domain.record.UserPasswordRequest;
import openclassroom.p6.paymybuddy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final String LOG_ID = "[UserController]";

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public String profileView(Model model) {

//        User user = userService.getPrincipal();
        User user = userService.getUser("test@test.com");

        UserInfoRequest userInfoRequest = new UserInfoRequest(user.getLastname(), user.getFirstname());
        UserPasswordRequest userPasswordRequest = new UserPasswordRequest("","","");
        AmountRequest amountRequest = new AmountRequest(0);

        logger.info("{} - user: {}, account balance: {}", LOG_ID, user.getEmail(), user.getBalance());

        model.addAttribute("user", user);
        model.addAttribute("userInfoRequest", userInfoRequest);
        model.addAttribute("userPasswordRequest", userPasswordRequest);
        model.addAttribute("amountRequest", amountRequest);
        model.addAttribute("breadcrumb", "Profile");

        return "profile";
    }

    @PostMapping("/info")
    public String saveUserInfo(
            Model model,
            Authentication authentication,
            @Valid UserInfoRequest userInfoRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes)
    {
//        User user = (User) authentication.getPrincipal();
        User user = userService.getUser("test@test.com");

        logger.info("{} - UserInfoRequest: {}", LOG_ID, userInfoRequest);

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> logger.error("{} - {}: {} - {}", LOG_ID, error.getObjectName(), error.getDefaultMessage(), error));

            UserPasswordRequest userPasswordRequest = new UserPasswordRequest("","","");
            AmountRequest amountRequest = new AmountRequest(0);

            model.addAttribute("userPasswordRequest", userPasswordRequest);
            model.addAttribute("amountRequest", amountRequest);
            model.addAttribute("user", user);
            model.addAttribute("breadcrumb", "Profile");

            return "profile";
        }

        boolean hasUserInfoChanged = userService.saveUserInfoRequest(userInfoRequest, user);
        if (hasUserInfoChanged) {
            redirectAttributes.addFlashAttribute("success", Messages.USER_INFO_SUCCESS);
        }

        return "redirect:/profile";
    }

    @PostMapping("/password")
    public String saveUserPassword(
            Model model,
            Authentication authentication,
            @Valid UserPasswordRequest userPasswordRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes)
    {
//        User user = (User) authentication.getPrincipal();
        User user = userService.getUser("test@test.com");

        logger.info("{} - userPwdRequest: {}", LOG_ID, userPasswordRequest);

        bindingResult = userService.validateUserPwdRequest(userPasswordRequest, user, bindingResult);

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> logger.error("{} - {}: {}", LOG_ID, error.getObjectName(), error.getDefaultMessage()));

            UserInfoRequest userInfoRequest = new UserInfoRequest(user.getLastname(), user.getFirstname());
            AmountRequest amountRequest = new AmountRequest(0);

            model.addAttribute("userInfoRequest", userInfoRequest);
            model.addAttribute("amountRequest", amountRequest);
            model.addAttribute("user", user);
            model.addAttribute("breadcrumb", "Profile");

            return "profile";
        }

        userService.saveUserPwdRequest(userPasswordRequest, user);
        redirectAttributes.addFlashAttribute("success", Messages.PASSWORD_SUCCESS);

        return "redirect:/profile";
    }
}
