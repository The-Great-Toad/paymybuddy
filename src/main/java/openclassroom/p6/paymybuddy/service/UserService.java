package openclassroom.p6.paymybuddy.service;

import openclassroom.p6.paymybuddy.domain.Contact;
import openclassroom.p6.paymybuddy.domain.User;
import openclassroom.p6.paymybuddy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final String LOG_ID = "[UserService]";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactService contactService;

    @Autowired
    private AccountService accountService;


    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
        // TODO: 02/05/2024 implements contact saving during successful user's registration
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
        contactService.deleteContact(user.getEmail());
    }

    public void removeContactFromUserContacts(User user, Contact contactToRemove) {

//        boolean isContactInUserContactList = user.getContacts().contains(contactToRemove);
//        if (isContactInUserContactList) {
//            logger.info("{} - Before removal - user contact list size: {}", LOG_ID, user.getContacts().size());
//            user.getContacts().remove(contactToRemove);
//            logger.info("{} - After removal - user contact list size: {}", LOG_ID, user.getContacts().size());
//            userRepository.save(user);
//        } else {
//            logger.error("{} - Contact not in User Contacts", LOG_ID);
//        }

        // todo: ask mentor why contact doesn't match from user's contacts list ?????

        List<Contact> contactList = new ArrayList<>();
        user.getContacts().forEach(contact -> {
            if (contact.getEmail().equals(contactToRemove.getEmail())) {
                logger.info("{} - Contact is in user's contact list", LOG_ID);
                contactList.add(contact);
            }
        });

        if (contactList.isEmpty()) {
            logger.error("{} - Contact not in User Contacts", LOG_ID);
        } else {
            logger.info("{} - removing contact '{}' from user's contacts list", LOG_ID, contactList.get(0).getEmail());
            user.getContacts().remove(contactList.get(0));
            userRepository.save(user);
        }
    }
}
