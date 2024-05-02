package openclassroom.p6.paymybuddy.repository;

import openclassroom.p6.paymybuddy.domain.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
}
