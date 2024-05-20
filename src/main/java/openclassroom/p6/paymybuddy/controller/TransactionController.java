package openclassroom.p6.paymybuddy.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import openclassroom.p6.paymybuddy.domain.Transaction;
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

import java.util.*;

@Controller
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
    private final String LOG_ID = "[TransactionController]";

    private final TransactionService transactionService;

    private final ContactService contactService;



    @GetMapping()
    public String transferView(
            Model model,
            Authentication authentication,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("{} - keyword: {}", LOG_ID, keyword);
        logger.info("{} - page number: {}", LOG_ID, page);
        logger.info("{} - pageSize: {}", LOG_ID, size);

        try {
            Pageable paging = PageRequest.of(page - 1, size, Sort.by("date").descending());
            Page<Transaction> pageTransactions = transactionService.getTransactions(keyword, paging);

            model.addAttribute("transactions", pageTransactions.getContent());
            model.addAttribute("contacts", contactService.getContacts());
//            model.addAttribute("transactionSuccess", false);
            model.addAttribute("transactionRequest", new TransactionRequest("","", ""));
            model.addAttribute("keyword", keyword);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalItems", pageTransactions.getTotalElements());
            model.addAttribute("totalPages", pageTransactions.getTotalPages());
            model.addAttribute("pageSize", size);
        } catch (Exception e) {
            model.addAttribute("exceptionMessage", e.getMessage());
        }

        return "transfer";
    }

    @PostMapping
    public String save(
            Model model,
            Authentication authentication,
            @Valid TransactionRequest transactionRequest,
            BindingResult bindingResult,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        logger.info("{} - transaction request: {}", LOG_ID, transactionRequest);
        if (bindingResult.hasErrors()) {
            return "transfer";
        }

        Transaction transaction = transactionService.saveTransactionRequest(authentication, transactionRequest);
        if (Objects.nonNull(transaction.getId())) {
            model.addAttribute("transactionSuccess", true);
        }

        try {
            Pageable paging = PageRequest.of(page - 1, size, Sort.by("date").descending());
            Page<Transaction> pageTransactions = transactionService.getTransactions(keyword, paging);

            model.addAttribute("transactions", pageTransactions.getContent());
            model.addAttribute("contacts", contactService.getContacts());
            model.addAttribute("keyword", keyword);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalItems", pageTransactions.getTotalElements());
            model.addAttribute("totalPages", pageTransactions.getTotalPages());
            model.addAttribute("pageSize", size);
            model.addAttribute("transactionRequest", new TransactionRequest("","", ""));
        }  catch (Exception e) {
            model.addAttribute("exceptionMessage", e.getMessage());
        }

        return "transfer";
    }
}
