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
        List<Contact> contacts = new ArrayList<>();
        try {
            contacts = contactRepository.findAll();
        } catch (Exception e) {
            logger.error("{} - {}", LOG_ID, e.getMessage());
        }
        return contacts;
    }

    public List<String> getUserContacts(String email) {
        logger.debug("{} - Retrieving contacts for user: {}", LOG_ID, email);
        List<Contact> userContactList;
        try {
            userContactList = contactRepository.findAllContactByUserEmail(email);

            if (userContactList.isEmpty()) {
                logger.info("{} - No contact found for user: {}", LOG_ID, email);
                return new ArrayList<>();
            }
            logger.info("{} - Retrieved {} contact(s) for user: {}", LOG_ID, userContactList.size(), email);

            return userContactList.stream()
                    .map(Contact::getEmail)
                    .toList();

        } catch (Exception e) {
            logger.error("{} - {}", LOG_ID, e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Contact> getRecentContacts(String email) {
        List<Contact> contacts = new ArrayList<>();

        try {
            contacts = contactRepository.findAllContactByUserEmail(email);
            logger.info("{} - Retrieved {} contacts for user: {}", LOG_ID, contacts.size(), email);

            if (contacts.size() <= 2) {
                return contacts;
            } else {
                return List.copyOf(contacts.subList(contacts.size() - 2, contacts.size()));
            }
        } catch (Exception e) {
            logger.error("{} - {}", LOG_ID, e.getMessage());
            return contacts;
        }
    }

    public Contact getContact(String email) {
        Optional<Contact> contact = contactRepository.findByEmail(email);

        if (contact.isPresent()) {
            logger.debug("{} - Retrieved contact: {}", LOG_ID, contact.get());
            return contact.get();
        }

        logger.error("{} - No contact found for email: {}", LOG_ID, email);
        return null;
    }

    public Contact saveContact(String email) {
        Contact savedContact = null;
        Contact newContact = Contact.builder()
                .email(email)
                .build();

        try {
            savedContact = contactRepository.save(newContact);
            logger.debug("{} - Saved contact: {}", LOG_ID, savedContact);
        } catch (Exception e) {
            logger.error("{} - {}", LOG_ID, e.getMessage());
        }

        return savedContact;
    }

    public Contact validateContactRequest(ContactRequest contactRequest) {
        Optional<Contact> optionalContact = contactRepository.findByEmail(contactRequest.email());
        return optionalContact.orElse(null);
    }
}
