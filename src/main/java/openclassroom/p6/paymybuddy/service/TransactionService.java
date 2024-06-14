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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
    private final String LOG_ID = "[TransactionService]";

    private final TransactionRepository transactionRepository;


    public Page<Transaction> getTransactions(String keyword, Pageable p, User user) {
        int accountId = user.getAccount().getId();
        Page<Transaction> transactions;

        if (keyword == null) {
            logger.info("{} - Retrieving transactions for {}, no keyword", LOG_ID, user.getEmail());
            transactions = transactionRepository.findAllByReceiverIdOrSenderIdOrderByDateDesc(
                    accountId,
                    accountId,
                    p);
        } else {
            logger.info("{} - Retrieving transactions for {}, with keyword {}", LOG_ID, user.getEmail(), keyword);
            transactions = transactionRepository.findAllByReceiverIdOrSenderIdAndDescriptionIsContainingIgnoreCaseOrderByDateDesc(
                    accountId,
                    accountId,
                    keyword,
                    p);
        }
        logger.info("{} - Retrieved {} transactions at page {}", LOG_ID, transactions.getContent().size(), transactions.getNumber());
        return transactions;
    }

    public Optional<Transaction> getTransaction(Integer id) {
        return transactionRepository.findById(id);
    }

    public Transaction saveTransaction(Transaction transaction) {
        logger.info("{} - Saving transaction {}", LOG_ID, transaction);
        return transactionRepository. save(transaction);
    }

    public Transaction saveTransactionRequest(Authentication authentication, TransactionRequest transactionRequest) {
        Transaction transaction = Stream.of(transactionRequest)
                .map(t -> Transaction.builder()
                        .date(LocalDateTime.now())
                        .description(t.description())
                        .amount(Double.parseDouble(t.amount()))
                        .build())
                .toList()
                .get(0);
        return saveTransaction(transaction);
    }
}
