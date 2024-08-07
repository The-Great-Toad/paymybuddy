package openclassroom.p6.paymybuddy.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import openclassroom.p6.paymybuddy.constante.Messages;
import openclassroom.p6.paymybuddy.domain.Transaction;
import openclassroom.p6.paymybuddy.domain.User;
import openclassroom.p6.paymybuddy.domain.record.TransactionRequest;
import openclassroom.p6.paymybuddy.service.ContactService;
import openclassroom.p6.paymybuddy.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
    private final String LOG_ID = "[TransactionController]";

    private final TransactionService transactionService;

    private final ContactService contactService;


    @GetMapping
    public String getTransactionView(
            Model model,
            Authentication authentication,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        logger.info("{} - keyword: {}, page number: {}, pageSize: {}", LOG_ID, keyword, page, size);
        User user = (User) authentication.getPrincipal();
//        User user = userService.getUser("test@test.com");

        setModelAttributes(model, keyword, page, size, user);
        model.addAttribute("transactionRequest", new TransactionRequest("","", ""));

        try {
            Pageable paging = PageRequest.of(page - 1, size, Sort.by("date").descending());
            Page<Transaction> pageTransactions = transactionService.getTransactions(keyword, paging, user.getEmail());

            model.addAttribute("transactions", pageTransactions.getContent());
            model.addAttribute("totalItems", pageTransactions.getTotalElements());
            model.addAttribute("totalPages", pageTransactions.getTotalPages());
        } catch (Exception e) {
            model.addAttribute("exceptionMessage", e.getMessage());
        }

        return "transaction";
    }

    @PostMapping
    public String saveTransaction(
            Model model,
            Authentication authentication,
            @Valid TransactionRequest transactionRequest,
            BindingResult bindingResult,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            RedirectAttributes redirectAttributes)
    {
        User user = (User) authentication.getPrincipal();
//        User user = userService.getUser("test@test.com");

        setModelAttributes(model, keyword, page, size, user);

        try {
            Pageable paging = PageRequest.of(page - 1, size, Sort.by("date").descending());
            Page<Transaction> pageTransactions;

            logger.info("{} - transaction request: {}", LOG_ID, transactionRequest);

            transactionService.verifyTransactionRequest(
                    user.getBalance(),
                    transactionRequest,
                    bindingResult
            );

            if (bindingResult.hasErrors()) {
                bindingResult.getAllErrors().forEach(error -> logger.error(error.getDefaultMessage()));
                pageTransactions = transactionService.getTransactions(keyword, paging, user.getEmail());
                model.addAttribute("transactions", pageTransactions.getContent());
                model.addAttribute("totalItems", pageTransactions.getTotalElements());
                model.addAttribute("totalPages", pageTransactions.getTotalPages());
                return "transaction";
            }

            transactionService.saveTransactionRequest(user, transactionRequest);

        }  catch (Exception e) {
            logger.error("{} - {}", LOG_ID, e.getMessage());
            model.addAttribute("exceptionMessage", e.getMessage());
        }

        redirectAttributes.addFlashAttribute("success", Messages.TRANSFER_SUCCESS);
        return "redirect:/transactions";
    }

    private void setModelAttributes(Model model, String keyword, int page, int size, User user) {
        model.addAttribute("user", user);
        model.addAttribute("contacts", contactService.getUserContacts(user.getEmail()));
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("breadcrumb", "Transfer");
    }
}
