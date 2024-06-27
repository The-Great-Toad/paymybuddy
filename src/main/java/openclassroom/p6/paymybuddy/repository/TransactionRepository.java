package openclassroom.p6.paymybuddy.repository;

import openclassroom.p6.paymybuddy.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    Page<Transaction> findTransactionsBySenderEmailOrReceiverEmailAndDescriptionIsContainingIgnoreCaseOrderByDateDesc(
            String senderEmail,
            String receiverEmail,
            String description,
            Pageable pageable);

    Page<Transaction> findTransactionsBySenderEmailOrReceiverEmail(String senderEmail, String receiverEmail, Pageable pageable);
}
