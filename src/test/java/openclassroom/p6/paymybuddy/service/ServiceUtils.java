package openclassroom.p6.paymybuddy.service;

import openclassroom.p6.paymybuddy.domain.Contact;
import openclassroom.p6.paymybuddy.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ServiceUtils {

    /* CONTACT UTILS */

    public Contact getContact(String email) {
        return new Contact(1, email, new ArrayList<>());
    }
    public Contact getContactLuc() {
        return new Contact(1, "luc@mail.com", new ArrayList<>());
    }

    public Contact getContactRob() {
        return new Contact(1, "rob@mail.com", new ArrayList<>());
    }

    public Contact getContactLucie() {
        return new Contact(1, "lucie@mail.com", new ArrayList<>());
    }

    public Optional<Contact> getOptionalContact(String email) {
        return Optional.of(getContact(email));
    }

    public Optional<Contact> getEmptyOptional() {
        return Optional.empty();
    }

    public List<Contact> getContacts() {
        return List.of(getContactLuc(), getContactRob(), getContactLucie());
    }

    /* TRANSACTION UTILS */

    public Transaction getTransaction(Double transactionAmount) {
        double amount = Objects.nonNull(transactionAmount) ? transactionAmount : 15;
        double fee = Math.floor((amount * Transaction.FEE_RATE) * 100) / 100;
        return Transaction.builder()
                .senderEmail("sender@mail.com")
                .receiverEmail("receiver@mail.com")
                .amount(amount)
                .fee(fee)
                .description("description")
                .date(LocalDateTime.now())
                .build();
    }

    public List<Transaction> getTransactions() {
        return List.of(
                getTransaction(10.0),
                getTransaction(35.0),
                getTransaction(175.0)
        );
    }

    public Page<Transaction> getPagedTransactions() {
        List<Transaction> transactions = getTransactions();
        return new PageImpl<>(transactions);
    }

}
