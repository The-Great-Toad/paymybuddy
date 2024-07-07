package openclassroom.p6.paymybuddy.service;

import openclassroom.p6.paymybuddy.domain.Contact;

import java.util.ArrayList;
import java.util.List;
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
}
