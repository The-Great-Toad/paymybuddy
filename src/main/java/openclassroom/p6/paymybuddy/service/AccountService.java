package openclassroom.p6.paymybuddy.service;

import openclassroom.p6.paymybuddy.domain.Account;
import openclassroom.p6.paymybuddy.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Iterable<Account> getAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccount(Integer id) {
        return accountRepository.findById(id);
    }

    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }
}
