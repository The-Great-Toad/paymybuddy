package openclassroom.p6.paymybuddy.integration;

import openclassroom.p6.paymybuddy.domain.Account;
import openclassroom.p6.paymybuddy.domain.Contact;
import openclassroom.p6.paymybuddy.domain.User;
import openclassroom.p6.paymybuddy.service.AccountService;
import openclassroom.p6.paymybuddy.service.ContactService;
import openclassroom.p6.paymybuddy.service.UserService;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ContactService contactService;

    private final String userEmail = "user.test@eamil.com";

//    @Test
//    @Transactional
//    @Commit
//    public void createUserTest() {
//
//        User newUser = new User()
//                .addEmail(userEmail)
//                .addFirstname("firstname")
//                .addLastname("lastname")
//                .addPassword("password");
//
//        newUser = userService.saveUser(newUser);
//        assertNotNull("User id is null", newUser.getId());
//
//        Account newAccount = Account.builder()
//                .balance(50000)
//                .user(newUser)
//                .build();
//
//        newAccount = accountService.saveAccount(newAccount);
//        assertNotNull("Account id is null", newAccount.getId());
//        assertTrue("User not linked to account", newAccount.getUser().getId().equals(newUser.getId()) );
//
////        assertTrue("User account not updated by relation", Objects.equals(newUser.getAccount().getId(), newAccount.getId()));
//
//        Optional<Contact> optionalContact = contactService.getContact(newUser.getEmail());
//        assertTrue("No contact created from relation", optionalContact.isPresent());
//        assertTrue("contact email saved incorrect", optionalContact.get().getEmail().equals(newUser.getEmail()));
//    }

    @Test
//    @Transactional
    public void deleteUserTest() {
        Optional<User> optionalUser = userService.getUser(userEmail);
        assertNotNull("No user in bdd", optionalUser);
        userService.deleteUser(optionalUser.get());

        Iterable<User> users = userService.getUsers();
        assertTrue("User found", IterableUtil.isNullOrEmpty(users));

        Iterable<Account> accounts = accountService.getAccounts();
        assertTrue("Account found", IterableUtil.isNullOrEmpty(accounts));

        Optional<Contact> optionalContact2 = contactService.getContact(userEmail);
        assertTrue("Contact found", optionalContact2.isEmpty());
    }
}
