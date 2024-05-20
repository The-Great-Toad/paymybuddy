package openclassroom.p6.paymybuddy.repository;

import openclassroom.p6.paymybuddy.domain.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {
}
