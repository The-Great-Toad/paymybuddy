package openclassroom.p6.paymybuddy.service;

import openclassroom.p6.paymybuddy.constante.Messages;
import openclassroom.p6.paymybuddy.domain.Account;
import openclassroom.p6.paymybuddy.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Optional;

@Service
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
    private static final String LOG_ID = "[AccountService]";

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

    public BindingResult validateWithdrawal(Account account, double withdrawal, BindingResult bindingResult) {

        if (withdrawal > account.getAvailable_balance()) {
            bindingResult.addError(new FieldError("amountRequest", "amount", Messages.ACCOUNT_INSUFFICIENT_FUNDS));
        }
        return bindingResult;
    }

    public void withdraw(Account account, double withdrawal) {
        double balance = account.getAvailable_balance();
        logger.info("{} - Withdrawing: ${} from account balance: ${}", LOG_ID, withdrawal, balance);
        account.setAvailable_balance(balance - withdrawal);
        logger.info("{} - Account balance after withdraw: ${}", LOG_ID, account.getAvailable_balance());
        accountRepository.save(account);
    }

    public void deposit(Account account, double deposit) {
        double balance = account.getAvailable_balance();
        logger.info("{} - Depositing: ${} to account balance: ${}", LOG_ID, deposit, balance);
        account.setAvailable_balance(balance + deposit);
        logger.info("{} - Account balance after deposit: ${}", LOG_ID, account.getAvailable_balance());
        accountRepository.save(account);
    }
}
