package openclassroom.p6.paymybuddy.repository;

import openclassroom.p6.paymybuddy.domain.Contact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Integer> {

    Optional<Contact> findByEmail(String email);
    void deleteContactByEmail(String email);
}
