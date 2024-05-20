package openclassroom.p6.paymybuddy.service;

import openclassroom.p6.paymybuddy.domain.Contact;
import openclassroom.p6.paymybuddy.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public Iterable<Contact> getContacts() {
        return contactRepository.findAll();
    }

    public Optional<Contact> getContact(String email) {
        return contactRepository.findByEmail(email);
    }

    public Contact saveContact(Contact contact) {
        return contactRepository.save(contact);
    }

    public void deleteContact(String email) {
        contactRepository.deleteContactByEmail(email);
    }

    public Contact createAndSaveContact(String email) {
        Contact contact = Contact.builder()
                .email(email)
                .build();
        return contactRepository.save(contact);
    }
}
