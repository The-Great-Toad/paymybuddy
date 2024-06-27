package openclassroom.p6.paymybuddy.repository;

import openclassroom.p6.paymybuddy.constante.Queries;
import openclassroom.p6.paymybuddy.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

    Optional<Contact> findByEmail(String email);

    @Query(value = Queries.GET_USER_CONTACTS, nativeQuery = true)
    List<Contact> findAllContactByUserEmail(String userEmail);

    void deleteContactByEmail(String email);

}
