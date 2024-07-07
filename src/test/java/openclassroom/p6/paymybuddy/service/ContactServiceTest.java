package openclassroom.p6.paymybuddy.service;

import openclassroom.p6.paymybuddy.domain.Contact;
import openclassroom.p6.paymybuddy.domain.record.ContactRequest;
import openclassroom.p6.paymybuddy.repository.ContactRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest extends ServiceUtils {

    @InjectMocks
    private ContactService contactService;

    @Mock
    private ContactRepository contactRepository;

    @Test
    void getContactsTest() {
        when(contactRepository.findAll()).thenReturn(getContacts());

        List<Contact> result = contactService.getContacts();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(getContactLuc()));
        assertTrue(result.contains(getContactRob()));
        assertTrue(result.contains(getContactLucie()));
    }

    @Test
    void getUserContactsTest() {
        String email = "test@test.com";

        when(contactRepository.findAllContactByUserEmail(email)).thenReturn(getContacts());

        List<String> result = contactService.getUserContacts(email);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(getContactLuc().getEmail()));
        assertTrue(result.contains(getContactRob().getEmail()));
        assertTrue(result.contains(getContactLucie().getEmail()));
    }

    @Test
    void getUserContactsTest_noContact() {
        String email = "test@test.com";

        when(contactRepository.findAllContactByUserEmail(email)).thenReturn(new ArrayList<>());

        List<String> result = contactService.getUserContacts(email);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getRecentContactsTest() {
        String email = "test@test.com";

        when(contactRepository.findAllContactByUserEmail(email)).thenReturn(getContacts());

        List<Contact> result = contactService.getRecentContacts(email);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(getContactRob()));
        assertTrue(result.contains(getContactLucie()));
    }

    @Test
    void getContactTest() {
        String email = "bob@mail.com";

        when(contactRepository.findByEmail(email))
                .thenReturn(getOptionalContact(email));

        Contact result = contactService.getContact(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void getContactTest_notFound() {
        String email = "not found";

        when(contactRepository.findByEmail(email))
                .thenReturn(getEmptyOptional());

        Contact result = contactService.getContact(email);

        assertNull(result);
    }

    @Test
    void saveContactTest() {
        Contact contact = getContactLuc();

        when(contactRepository.save(contact)).thenReturn(contact);

        Contact result = contactService.saveContact(contact.getEmail());

        assertNotNull(result);
        assertEquals(contact, result);
    }

    @Test
    void validateContactRequestTest() {
        String email = "test@test.com";
        ContactRequest contactRequest = new ContactRequest(email);

        when(contactRepository.findByEmail(email)).thenReturn(getOptionalContact(email));
        Contact result = contactService.validateContactRequest(contactRequest);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void validateContactRequestTest_NotFound() {
        String email = "not found";
        ContactRequest contactRequest = new ContactRequest(email);

        when(contactRepository.findByEmail(email)).thenReturn(getEmptyOptional());
        Contact result = contactService.validateContactRequest(contactRequest);

        assertNull(result);
    }
}