package openclassroom.p6.paymybuddy.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import openclassroom.p6.paymybuddy.domain.Contact;
import openclassroom.p6.paymybuddy.domain.Transaction;
import openclassroom.p6.paymybuddy.domain.User;
import openclassroom.p6.paymybuddy.domain.record.UserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class InitDB {

    private static final Logger logger = LoggerFactory.getLogger(InitDB.class);
    private final List<String> descriptionList = List.of("Restaurant bill share","Trip money","Movie tickets","Bowling game","Grocery shopping","Gasoline expenses","Coffee date","Concert tickets","Online shopping","Fitness class fee","Taxi fare","Birthday gift","Book purchase","Charity donation","Vacation rental payment");
    private final List<Double> amountList = List.of(2.36, 1.75, 4.56, 3.97, 1.84);

    @Value("${spring.filepath.users}")
    private String usersFilePath;

    private final ObjectMapper objectMapper;
    private final TransactionService transactionService;
    private final ContactService contactService;
    private final UserService userService;

    public void initTables() {
        logger.info("[DATABASE INITIALIZATION] - START");

        initUsersTable();
        initUserContactTable();
        initTransactionTable();

        logger.info("[DATABASE INITIALIZATION] - END");

    }

    public void initUsersTable() {
        final String METHOD_ID = "[USERS TABLE INITIALIZATION]";
        List<UserRequest> users;
        int[] nbUsersSaved = { 0 };
        int[] nbUsersFailed = { 0 };

        logger.info("{} - START", METHOD_ID);

        try {
            users = objectMapper.readValue(new File(usersFilePath), new TypeReference<>() {});
        } catch (IOException e) {
            logger.error("{} - {}", METHOD_ID, e.getMessage());
            users = Collections.emptyList();
        }

        if (!users.isEmpty()) {
            users.forEach(user -> {
                User temp = userService.registerNewUser(user);
                if (temp != null) {
                    nbUsersSaved[0]++;
                } else {
                    nbUsersFailed[0]++;
                }
            });
        }
        logger.info("{} - {} users saved, {} failed to save", METHOD_ID, nbUsersSaved, nbUsersFailed);
        logger.info("{} - END", METHOD_ID);
    }

    private void initTransactionTable() {
        final String METHOD_ID = "[TRANSACTIONS TABLE INITIALIZATION]";
        int[] nbTransactionsSaved = { 0 };
        int[] nbTransactionsFailed = { 0 };

        logger.info("{} - START", METHOD_ID);
        logger.info("{} - Retrieving contacts...", METHOD_ID);

        List<Contact> contacts = contactService.getContacts();

        if (!contacts.isEmpty()) {
            logger.info("{} - {} contacts retrieved", METHOD_ID, contacts.size());
            logger.info("{} - Transaction creation start...", METHOD_ID);

            int indexSender = 0;
            for (int i = 0; i < 200; i++) {

                int indexReceiver;
                if (i == 20 || i == 40 || i == 60 || i == 80 || i == 100 || i == 120 || i == 140 || i == 160 || i == 180) ++indexSender;
                do {
                    indexReceiver = new Random().nextInt(10);
                } while (indexReceiver == indexSender);

                int randomDayNb = new Random().nextInt(100) + 1;
                int randomHourNb = new Random().nextInt(22) + 1;
                int randomDescriptionIndex = new Random().nextInt(descriptionList.size() - 1);
                String description = descriptionList.get(randomDescriptionIndex);
                double amount = amountList.get(new Random().nextInt(amountList.size() - 1)) * (new Random().nextInt(20)+1);
                double fee = Math.floor((amount * Transaction.FEE_RATE) * 100) / 100;

                Transaction transaction = Transaction.builder()
                        .senderEmail(contacts.get(indexSender).getEmail())
                        .receiverEmail(contacts.get(indexReceiver).getEmail())
                        .description(description)
                        .amount((Math.floor((amount + fee) * 100) / 100))
                        .fee(fee)
                        .date(LocalDateTime.ofInstant(
                                Instant.now().minus(randomDayNb, ChronoUnit.DAYS).minus(randomHourNb, ChronoUnit.HOURS),
                                ZoneOffset.UTC)
                        ).build();

                transaction = transactionService.saveTransaction(transaction);

                if (Objects.nonNull(transaction)) {
                    nbTransactionsSaved[0]++;
                } else {
                    nbTransactionsFailed[0]++;
                }
            }
        }

        logger.info("{} - Created & saved {} transactions, {} failed transactions", METHOD_ID, nbTransactionsSaved, nbTransactionsFailed);
        logger.info("{} - END", METHOD_ID);
    }

    private void initUserContactTable() {
        final String METHOD_ID = "[USER_CONTACT TABLE INITIALIZATION]";
        logger.info("{} - START - User contact table initialization", METHOD_ID);

        Iterable<User> users = userService.getUsers();
        long userNb = StreamSupport.stream(users.spliterator(), false).count();
        logger.info("{} - Retrieved {} users from db", METHOD_ID, userNb);

        Iterable<Contact> contacts = contactService.getContacts();
        long contactNb = StreamSupport.stream(contacts.spliterator(), false).count();
        logger.info("{} - Retrieved {} contacts", METHOD_ID, contactNb);

        for (User user : users) {
            List<Contact> contactList = new ArrayList<>();
            contacts.forEach(contactList::add);
            contactList.remove(contactService.getContact(user.getEmail()));
            Collections.shuffle(contactList);

            user.setContacts(contactList.subList(0,5));
            userService.save(user);
//            user.getContacts().forEach(contact -> logger.info("{} - Adding contact {} to user {}", METHOD_ID, contact.getEmail(), user.getEmail()));
        }
        logger.info("{} - END - User contact table initialization", METHOD_ID);
    }
}
