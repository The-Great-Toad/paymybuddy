package openclassroom.p6.paymybuddy.controller;

import openclassroom.p6.paymybuddy.domain.User;
import openclassroom.p6.paymybuddy.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootTest
public class ControllerUtils {

    @Autowired
    private UserService userService;

    public PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    protected final User user = createTestUser();

    private User createTestUser() {
        return User.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john@doe.com")
                .password(passwordEncoder.encode("password"))
                .balance(5000.0)
                .role(User.Role.USER)
                .contacts(new ArrayList<>())
                .build();
    }

    @BeforeEach
    void setUp() {
        userService.save(user);
    }

    @AfterEach
    void tearDown() {
        userService.delete(user);
    }
}
