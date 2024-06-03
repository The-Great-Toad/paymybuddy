package openclassroom.p6.paymybuddy.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import openclassroom.p6.paymybuddy.constante.Messages;
import openclassroom.p6.paymybuddy.domain.User;
import openclassroom.p6.paymybuddy.domain.record.AmountRequest;
import openclassroom.p6.paymybuddy.domain.record.UserInfoRequest;
import openclassroom.p6.paymybuddy.domain.record.UserPasswordRequest;
import openclassroom.p6.paymybuddy.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    private static final String LOG_ID = "[AccountController]";

    private final AccountService accountService;

    @PostMapping("/withdraw")
    public String withdraw(
            Authentication authentication,
            Model model,
            @Valid AmountRequest withdrawal,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes)
    {
        logger.info("{} - withdraw amountRequest: {}", LOG_ID, withdrawal);
        User user = (User) authentication.getPrincipal();

        bindingResult = accountService.validateWithdrawal(user.getAccount(), withdrawal.amount(), bindingResult);

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> logger.error("{} - {}", LOG_ID, error));
            UserInfoRequest userInfoRequest = new UserInfoRequest(user.getLastname(), user.getFirstname());
            UserPasswordRequest userPasswordRequest = new UserPasswordRequest("","","");

            model.addAttribute("user", user);
            model.addAttribute("userInfoRequest", userInfoRequest);
            model.addAttribute("userPasswordRequest", userPasswordRequest);

            return "profile";
        }

        accountService.withdraw(user.getAccount(), withdrawal.amount());
        redirectAttributes.addFlashAttribute("success", Messages.WITHDRAW_SUCCESS);

        return "redirect:/profile";
    }

    @PostMapping("/deposit")
    public String deposit(
            Authentication authentication,
            Model model,
            @Valid AmountRequest deposit,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes)
    {
        logger.info("{} - deposit amountRequest: {}", LOG_ID, deposit);
        User user = (User) authentication.getPrincipal();

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> logger.error("{} - {}", LOG_ID, error));
            UserInfoRequest userInfoRequest = new UserInfoRequest(user.getLastname(), user.getFirstname());
            UserPasswordRequest userPasswordRequest = new UserPasswordRequest("","","");

            model.addAttribute("user", user);
            model.addAttribute("userInfoRequest", userInfoRequest);
            model.addAttribute("userPasswordRequest", userPasswordRequest);

            return "profile";
        }

        accountService.deposit(user.getAccount(), deposit.amount());
        redirectAttributes.addFlashAttribute("success", Messages.DEPOSIT_SUCCESS);

        return "redirect:/profile";
    }
}
