package openclassroom.p6.paymybuddy.service;

import lombok.RequiredArgsConstructor;
import openclassroom.p6.paymybuddy.constante.Messages;
import openclassroom.p6.paymybuddy.domain.Transaction;
import openclassroom.p6.paymybuddy.domain.record.TransactionRequest;
import openclassroom.p6.paymybuddy.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
    private final String LOG_ID = "[TransactionService]";

    private final TransactionRepository transactionRepository;


    public Page<Transaction> getTransactions(String keyword, Pageable p, String userEmail) {
        Page<Transaction> transactions;

        if (keyword == null) {
            logger.info("{} - Retrieving transactions for {}, no keyword", LOG_ID, userEmail);
            transactions = transactionRepository.findTransactionsBySenderEmailOrReceiverEmail(
                    userEmail,
                    userEmail,
                    p);
        } else {
            logger.info("{} - Retrieving transactions for {}, with keyword {}", LOG_ID, userEmail, keyword);
            transactions = transactionRepository.findTransactionsBySenderEmailOrReceiverEmailAndDescriptionIsContainingIgnoreCaseOrderByDateDesc(
                    userEmail,
                    userEmail,
                    keyword,
                    p);
        }
        logger.info("{} - Retrieved {} transactions at page {}", LOG_ID, transactions.getContent().size(), transactions.getNumber());
        return transactions;
    }

    public List<Transaction> getRecentTransactions(String email) {
        List<Transaction> recentTransactions = transactionRepository.findRecentTransactions(email);

        if (recentTransactions.isEmpty()) {
            logger.info("{} - No recent transactions found for email {}", LOG_ID, email);
            return new ArrayList<>();
        } else {
            logger.info("{} - Found {} recent transactions for email {}", LOG_ID, recentTransactions.size(), email);
            return recentTransactions;
        }
    }

    public Transaction saveTransactionRequest(String userEmail, TransactionRequest transactionRequest) {
        double amount = Math.floor(Double.parseDouble(transactionRequest.amount()) * 100) / 100;
        double fee = Math.floor((amount * Transaction.FEE_RATE) * 100) / 100;

        Transaction transaction = Stream.of(transactionRequest)
                .map(t -> Transaction.builder()
                        .senderEmail(userEmail)
                        .receiverEmail(t.receiver())
                        .description(t.description())
                        .amount(amount)
                        .fee(fee)
                        .date(LocalDateTime.now())
                        .build())
                .toList()
                .get(0);

        return saveTransaction(transaction);
    }

    public Transaction saveTransaction(Transaction transaction) {
        logger.debug("{} - Saving transaction {}", LOG_ID, transaction);
        return transactionRepository.save(transaction);
    }

    public BindingResult verifyTransactionRequest(Double userBalance, TransactionRequest transactionRequest, BindingResult bindingResult) {
        if (Objects.nonNull(transactionRequest.amount())) {
            double amount = Math.floor(Double.parseDouble(transactionRequest.amount()) * 100) / 100;
            if (amount >= userBalance) {
                bindingResult.addError(new FieldError(
                        "transactionRequest",
                        "amount",
                        Messages.ACCOUNT_INSUFFICIENT_FUNDS
                ));
            }
        }
        return bindingResult;
    }
}
