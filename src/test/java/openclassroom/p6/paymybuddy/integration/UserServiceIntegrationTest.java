package openclassroom.p6.paymybuddy.integration;

import openclassroom.p6.paymybuddy.service.ContactService;
import openclassroom.p6.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

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
}
