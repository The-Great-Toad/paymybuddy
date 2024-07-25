package openclassroom.p6.paymybuddy.service;

import openclassroom.p6.paymybuddy.domain.Transaction;
import openclassroom.p6.paymybuddy.domain.record.TransactionRequest;
import openclassroom.p6.paymybuddy.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest extends ServiceUtils {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserService userService;

    @Test
    void getTransactionsTest_keywordNull() {
        String keyword = null;
        Pageable pageable = Pageable.unpaged();
        String email = "test@test.com";

        when(transactionRepository.findTransactionsBySenderEmailOrReceiverEmail(email, email, pageable))
                .thenReturn(getPagedTransactions());

        Page<Transaction> result = transactionService.getTransactions(
                keyword,
                pageable,
                email
        );

        assertNotNull(result);
        assertEquals(getPagedTransactions().getTotalElements(), result.getTotalElements());
        assertEquals(getPagedTransactions().getTotalPages(), result.getTotalPages());
        assertEquals(getPagedTransactions().getNumber(), result.getNumber());
        assertEquals(getPagedTransactions().getSize(), result.getSize());
    }

    @Test
    void getTransactionsTest_withKeyword() {
        String keyword = "keyword";
        Pageable pageable = Pageable.unpaged();
        String email = "test@test.com";

        when(transactionRepository.findTransactionsBySenderEmailOrReceiverEmailAndDescriptionIsContainingIgnoreCaseOrderByDateDesc(
                email, email, keyword, pageable)).thenReturn(getPagedTransactions());

        Page<Transaction> result = transactionService.getTransactions(
                keyword,
                pageable,
                email
        );

        assertNotNull(result);
        assertEquals(getPagedTransactions().getTotalElements(), result.getTotalElements());
        assertEquals(getPagedTransactions().getTotalPages(), result.getTotalPages());
        assertEquals(getPagedTransactions().getNumber(), result.getNumber());
        assertEquals(getPagedTransactions().getSize(), result.getSize());
    }

    @Test
    void getRecentTransactionsTest() {
        String email = "test@test.com";
        List<Transaction> expectedResult = getTransactions();

        when(transactionRepository.findRecentTransactions(email)).thenReturn(expectedResult);

        List<Transaction> result = transactionService.getRecentTransactions(email);

        assertNotNull(result);
        assertEquals(expectedResult.size(), result.size());
        assertEquals(expectedResult.get(0), result.get(0));
        assertEquals(expectedResult, result);
    }

    @Test
    void getRecentTransactionsTest_NoTransactions() {
        String email = "test@test.com";
        List<Transaction> expectedResult = new ArrayList<>();

        when(transactionRepository.findRecentTransactions(email)).thenReturn(expectedResult);

        List<Transaction> result = transactionService.getRecentTransactions(email);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(expectedResult, result);
    }

    @Test
    void saveTransactionRequestTest() {
        String email = "sender@mail.com";
        TransactionRequest transactionRequest = new TransactionRequest(
                "receiver@mail.com", "description", "25"
        );
        Transaction expectedResult = getTransaction(25.0);

        when(transactionRepository.save(any())).thenReturn(expectedResult);

        Transaction result = transactionService.saveTransactionRequest(email, transactionRequest);

        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    @Test
    void saveTransactionTest() {
        Transaction expectedResult = getTransaction(25.0);

        when(transactionRepository.save(any())).thenReturn(expectedResult);

        Transaction result = transactionService.saveTransaction(expectedResult);

        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    @Test
    void verifyTransactionRequestTest() {
        double balance = 25.0;
        TransactionRequest transactionRequest = new TransactionRequest(
                "receiver@mail.com", "description", "25"
        );
        BindingResult bindingResult = new BeanPropertyBindingResult(transactionRequest, "transactionRequest");

        BindingResult result = transactionService.verifyTransactionRequest(balance, transactionRequest, bindingResult);

        assertNotNull(result);
        assertTrue(result.hasErrors());
        assertTrue(result.hasFieldErrors("amount"));
    }
}