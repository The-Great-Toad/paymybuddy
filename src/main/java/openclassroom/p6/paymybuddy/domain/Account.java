package openclassroom.p6.paymybuddy.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;
import java.util.Objects;

@Entity
@DynamicUpdate
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.PERSIST
            }
    )
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private double balance;

    @Column
    private double available_balance;

    @Column
    private double pending_balance;


    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }
    )
    @JoinTable(
            name = "account_transaction",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "transaction_id")
    )
    private List<Transaction> transactions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Account addBalance(double balance) {
        this.balance = balance;
        return this;
    }

    public double getAvailable_balance() {
        return available_balance;
    }

    public void setAvailable_balance(double available_balance) {
        this.available_balance = available_balance;
    }

    public Account addAvailable_balance(double available_balance) {
        this.available_balance = available_balance;
        return this;
    }

    public double getPending_balance() {
        return pending_balance;
    }

    public void setPending_balance(double pending_balance) {
        this.pending_balance = pending_balance;
    }

    public Account addPending_balance(double pending_balance) {
        this.pending_balance = pending_balance;
        return this;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Account addUser(User user) {
        this.user = user;
        return this;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addSenderTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void removeSenderTransaction(Transaction transaction) {
        transactions.remove(transaction);
    }

    public void addReceiverTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void removeReceiverTransaction(Transaction transaction) {
        transactions.remove(transaction);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Double.compare(account.balance, balance) == 0 && Double.compare(account.available_balance, available_balance) == 0 && Double.compare(account.pending_balance, pending_balance) == 0 && Objects.equals(id, account.id)
                && Objects.equals(user, account.user)
                && Objects.equals(transactions, account.transactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, balance, available_balance, pending_balance,
                user,
                transactions);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Account{");
        sb.append("id=").append(id);
        sb.append(", balance=").append(balance);
        sb.append(", available_balance=").append(available_balance);
        sb.append(", pending_balance=").append(pending_balance);
        sb.append(", user=").append(user);
        sb.append(", transactions=").append(transactions);
        sb.append('}' );
        return sb.toString();
    }
}
