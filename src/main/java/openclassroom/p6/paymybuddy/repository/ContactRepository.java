package openclassroom.p6.paymybuddy.repository;

import openclassroom.p6.paymybuddy.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

    @Query(value = "SELECT * FROM contact c WHERE id IN (SELECT contact_id FROM user_contact uc WHERE user_id = :id)", nativeQuery = true)
    List<Contact> findUserContact(int id);

    Optional<Contact> findByEmail(String email);

    void deleteContactByEmail(String email);
}
