package openclassroom.p6.paymybuddy.service;

import lombok.RequiredArgsConstructor;
import openclassroom.p6.paymybuddy.domain.Transaction;
import openclassroom.p6.paymybuddy.domain.User;
import openclassroom.p6.paymybuddy.domain.record.TransactionRequest;
import openclassroom.p6.paymybuddy.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
    private final String LOG_ID = "[TransactionService]";

    private final TransactionRepository transactionRepository;


    public Page<Transaction> getTransactions(String keyword, Pageable p, User user) {
        String email = user.getEmail();
        Page<Transaction> transactions;

        if (keyword == null) {
            logger.info("{} - Retrieving transactions for {}, no keyword", LOG_ID, user.getEmail());
            transactions = transactionRepository.findTransactionsBySenderEmailOrReceiverEmail(
                    email,
                    email,
                    p);
        } else {
            logger.info("{} - Retrieving transactions for {}, with keyword {}", LOG_ID, user.getEmail(), keyword);
            transactions = transactionRepository.findTransactionsBySenderEmailOrReceiverEmailAndDescriptionIsContainingIgnoreCaseOrderByDateDesc(
                    email,
                    email,
                    keyword,
                    p);
        }
        logger.info("{} - Retrieved {} transactions at page {}", LOG_ID, transactions.getContent().size(), transactions.getNumber());
        return transactions;
    }

    public Transaction saveTransactionRequest(User user, TransactionRequest transactionRequest) {
        double amount = Math.floor(Double.parseDouble(transactionRequest.amount()) * 100) / 100;
        double fee = Math.floor((amount * Transaction.FEE_RATE) * 100) / 100;
        Transaction transaction = Stream.of(transactionRequest)
                .map(t -> Transaction.builder()
                        .senderEmail(user.getEmail())
                        .receiverEmail(t.receiver())
                        .description(t.description())
                        .amount(amount + fee)
                        .fee(fee)
                        .date(LocalDateTime.now())
                        .build())
                .toList()
                .get(0);
        return saveTransaction(transaction);
    }

    public Transaction saveTransaction(Transaction transaction) {
        logger.info("{} - Saving transaction {}", LOG_ID, transaction);
        return transactionRepository. save(transaction);
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
}
