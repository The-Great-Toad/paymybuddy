package openclassroom.p6.paymybuddy.service;

import openclassroom.p6.paymybuddy.domain.Contact;
import openclassroom.p6.paymybuddy.domain.record.ContactRequest;
import openclassroom.p6.paymybuddy.repository.ContactRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    private static final Logger logger = LoggerFactory.getLogger(ContactService.class);
    private final String LOG_ID = "[ContactService]";

    @Autowired
    private ContactRepository contactRepository;

    public Iterable<Contact> getContacts() {
        return contactRepository.findAll();
    }

    public List<Contact> getUserContacts(int id) { return contactRepository.findUserContact(id); }

    public Optional<Contact> getContact(String email) {
        return contactRepository.findByEmail(email);
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
}
