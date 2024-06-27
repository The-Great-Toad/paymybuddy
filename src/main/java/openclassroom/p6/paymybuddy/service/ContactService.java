package openclassroom.p6.paymybuddy.service;

import openclassroom.p6.paymybuddy.domain.Contact;
import openclassroom.p6.paymybuddy.domain.record.ContactRequest;
import openclassroom.p6.paymybuddy.repository.ContactRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    private static final Logger logger = LoggerFactory.getLogger(ContactService.class);
    private final String LOG_ID = "[ContactService]";

    @Autowired
    private ContactRepository contactRepository;

    public List<Contact> getContacts() {
        return List.copyOf(contactRepository.findAll());
    }

    public List<String> getUserContactList(String email) {
        logger.debug("{} - Retrieving contacts for user: {}", LOG_ID, email);
        List<Contact> userContactList = contactRepository.findAllContactByUserEmail(email);

        if (userContactList.isEmpty()) {
            logger.info("{} - No contact found for user: {}", LOG_ID, email);
            return new ArrayList<>();
        }
        logger.info("{} - Retrieved {} contact(s) for user: {}", LOG_ID, userContactList.size(), email);

        return userContactList.stream()
                .map(Contact::getEmail)
                .toList();
    }

    public Contact getContact(String email) {
        Optional<Contact> contact = contactRepository.findByEmail(email);

        if (contact.isPresent()) {
            logger.info("{} - Retrieved contact: {}", LOG_ID, contact.get());
            return contact.get();
        }

        logger.error("{} - No contact found for email: {}", LOG_ID, email);
        return null;
    }

    public void deleteContact(String email) {
        contactRepository.deleteContactByEmail(email);
    }

    public Contact validateContactRequest(ContactRequest contactRequest) {
        Optional<Contact> optionalContact = contactRepository.findByEmail(contactRequest.email());
        return optionalContact.orElse(null);
    }

    public Contact saveContact(String email) {
        Contact newContact = Contact.builder()
                .email(email)
                .build();

        return contactRepository.save(newContact);
    }

    public List<Contact> getRecentContacts(String email) {
        return new ArrayList<>();
        //todo
    }
}
