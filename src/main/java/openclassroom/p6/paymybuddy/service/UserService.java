package openclassroom.p6.paymybuddy.service;

import openclassroom.p6.paymybuddy.domain.Contact;
import openclassroom.p6.paymybuddy.domain.User;
import openclassroom.p6.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

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
        User savedUser = userRepository.save(user);
        Contact contact = contactService.createAndSaveContact(user.getEmail());
        // TODO: 02/05/2024 check if contact saved successfully
        return savedUser;
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
        contactService.deleteContact(user.getEmail());
    }
}
