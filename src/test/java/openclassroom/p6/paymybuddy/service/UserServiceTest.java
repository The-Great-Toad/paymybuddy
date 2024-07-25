package openclassroom.p6.paymybuddy.service;

import openclassroom.p6.paymybuddy.constante.Messages;
import openclassroom.p6.paymybuddy.domain.User;
import openclassroom.p6.paymybuddy.domain.record.AmountRequest;
import openclassroom.p6.paymybuddy.domain.record.UserInfoRequest;
import openclassroom.p6.paymybuddy.domain.record.UserPasswordRequest;
import openclassroom.p6.paymybuddy.domain.record.UserRequest;
import openclassroom.p6.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest extends ServiceUtils {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ContactService contactService;

    @Bean
    private BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Test
    void saveTest() {
        User expectedResult = new User();

        when(userRepository.save(expectedResult)).thenReturn(expectedResult);

        User result = userService.save(expectedResult);

        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    @Test
    void getUsersTest() {
        Iterable<User> expectedResult = List.of(new User());

        when(userRepository.findAll()).thenReturn(expectedResult);

        Iterable<User> result = userService.getUsers();

        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    @Test
    void getUserTest() {
        Optional<User> expectedResult = Optional.of(new User());

        when(userRepository.findByEmail(anyString())).thenReturn(expectedResult);

        User result = userService.getUser("email");

        assertNotNull(result);
        assertEquals(expectedResult.get(), result);
    }

    @Test
    void getUserTest_NotFound() {
        Optional<User> expectedResult = Optional.empty();

        when(userRepository.findByEmail(anyString())).thenReturn(expectedResult);

        User result = userService.getUser("email");

        assertNull(result);
    }

    @Test
    void getPrincipalTest() {
        User expectedResult = User.builder()
                .email("principal")
                .build();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(expectedResult, "password")
        );

        User result = userService.getPrincipal();

        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    @Test
    void logoutUserTest() {
        User expectedResult = User.builder()
                .email("principal")
                .build();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(expectedResult, "password")
        );

        User result = userService.getPrincipal();

        assertNotNull(result);
        assertEquals(expectedResult, result);

        userService.logoutUser();

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void registerNewUserTest() {
        UserRequest userRequest = new UserRequest(
                "lastname", "firstname", "email", "password"
        );
        User expectedResult = new User();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(contactService.saveContact(anyString())).thenReturn(getContact("email"));
        when(userRepository.save(any())).thenReturn(expectedResult);

        User result = userService.registerNewUser(userRequest);

        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    @Test
    void registerNewUserTest_EmailAlreadyExist() {
        UserRequest userRequest = new UserRequest(
                "lastname", "firstname", "email", "password"
        );

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        User result = userService.registerNewUser(userRequest);

        assertNull(result);
    }

    @Test
    void saveUserInfoRequestTest() {
        UserInfoRequest userInfoRequest = new UserInfoRequest("lastname", "firstname");
        User user = User.builder()
                .lastname("Sponge")
                .firstname("Bob")
                .build();

        when(userRepository.save(user)).thenReturn(user);

        boolean result = userService.saveUserInfoRequest(userInfoRequest, user);

        assertTrue(result);
    }

    @Test
    void saveUserInfoRequestTest_InfoNotModified() {
        UserInfoRequest userInfoRequest = new UserInfoRequest("lastname", "firstname");
        User user = User.builder()
                .lastname("lastname")
                .firstname("firstname")
                .build();

        boolean result = userService.saveUserInfoRequest(userInfoRequest, user);

        assertFalse(result);
    }

    @Test
    void saveUserPwdRequestTest() {
        UserPasswordRequest userPasswordRequest = new UserPasswordRequest(
                "oldpassword", "newpassword", "confirmpassword"
        );
        User user = new User();

        assertDoesNotThrow(() -> userService.saveUserPwdRequest(userPasswordRequest, user));
    }

    @Test
    void withdrawTest() {
        User user = new User();
        double withdrawAmount = 100.0;
        assertDoesNotThrow(() -> userService.withdraw(user, withdrawAmount));
    }

    @Test
    void depositTest() {
        User user = new User();
        double deposit = 100.0;
        assertDoesNotThrow(() -> userService.deposit(user, deposit));
    }

    @Test
    void validateWithdrawalTest() {
        double balance = 100.0;
        double withdrawAmount = 50.0;
        AmountRequest amountRequest = new AmountRequest(withdrawAmount);
        BindingResult bindingResult = new BeanPropertyBindingResult(amountRequest, "user");

        BindingResult result = userService.validateWithdrawal(balance, withdrawAmount, bindingResult);

        assertNotNull(result);
        assertFalse(result.hasErrors());
    }

    @Test
    void validateWithdrawalTest_InsufficientFunds() {
        double balance = 100.0;
        double withdrawAmount = 110.0;
        AmountRequest amountRequest = new AmountRequest(withdrawAmount);
        BindingResult bindingResult = new BeanPropertyBindingResult(amountRequest, "amountRequest");

        BindingResult result = userService.validateWithdrawal(balance, withdrawAmount, bindingResult);

        assertNotNull(result);
        assertTrue(result.hasErrors());
        assertEquals(Messages.ACCOUNT_INSUFFICIENT_FUNDS, Objects.requireNonNull(result.getFieldError("amount")).getDefaultMessage());
    }

    @Test
    void validateUserPwdRequestTest() {
        UserPasswordRequest userPasswordRequest = new UserPasswordRequest(
                "oldpassword", "Password!123", "Password!123"
        );
        User user = User.builder()
                .password(getPasswordEncoder().encode("oldpassword"))
                .build();
        BindingResult bindingResult = new BeanPropertyBindingResult(userPasswordRequest, "userPasswordRequest");

        BindingResult result = userService.validateUserPwdRequest(userPasswordRequest, user, bindingResult);

        assertNotNull(result);
        assertFalse(result.hasErrors());
    }

    @Test
    void validateUserPwdRequestTest_oldPasswordNotMatch() {
        UserPasswordRequest userPasswordRequest = new UserPasswordRequest(
                "oldpassword", "Password!123", "Password!123"
        );
        User user = User.builder()
                .password(getPasswordEncoder().encode("currentpassword"))
                .build();
        BindingResult bindingResult = new BeanPropertyBindingResult(userPasswordRequest, "userPasswordRequest");

        BindingResult result = userService.validateUserPwdRequest(userPasswordRequest, user, bindingResult);

        assertNotNull(result);
        assertTrue(result.hasErrors());
        assertTrue(result.hasFieldErrors("oldPassword"));
    }

    @Test
    void validateUserPwdRequestTest_NewPasswordDoesntRespectPasswordPolicy() {
        UserPasswordRequest userPasswordRequest = new UserPasswordRequest(
                "oldpassword", "password", "password"
        );
        User user = User.builder()
                .password(getPasswordEncoder().encode("oldpassword"))
                .build();
        BindingResult bindingResult = new BeanPropertyBindingResult(userPasswordRequest, "userPasswordRequest");

        BindingResult result = userService.validateUserPwdRequest(userPasswordRequest, user, bindingResult);

        assertNotNull(result);
        assertTrue(result.hasErrors());
        assertTrue(result.hasFieldErrors("newPassword"));
    }

    @Test
    void validateUserPwdRequestTest_ConfirmPasswordNotMatch() {
        UserPasswordRequest userPasswordRequest = new UserPasswordRequest(
                "oldpassword", "Password!123", "password"
        );
        User user = User.builder()
                .password(getPasswordEncoder().encode("oldpassword"))
                .build();
        BindingResult bindingResult = new BeanPropertyBindingResult(userPasswordRequest, "userPasswordRequest");

        BindingResult result = userService.validateUserPwdRequest(userPasswordRequest, user, bindingResult);

        assertNotNull(result);
        assertTrue(result.hasErrors());
        assertTrue(result.hasFieldErrors("confirmPassword"));
    }

    @Test
    void validateUserInfoRequestTest() {
        UserInfoRequest userInfoRequest = new UserInfoRequest("lastname", "firstname");
        BindingResult bindingResult = new BeanPropertyBindingResult(userInfoRequest, "userInfoRequest");

        BindingResult result = userService.validateUserInfoRequest(userInfoRequest, bindingResult);

        assertNotNull(result);
        assertFalse(result.hasErrors());
    }

    @Test
    void validateUserInfoRequestTest_InvalidChar() {
        UserInfoRequest userInfoRequest = new UserInfoRequest("123", "456");
        BindingResult bindingResult = new BeanPropertyBindingResult(userInfoRequest, "userInfoRequest");

        BindingResult result = userService.validateUserInfoRequest(userInfoRequest, bindingResult);

        assertNotNull(result);
        assertTrue(result.hasErrors());
        assertTrue(result.hasFieldErrors("lastname"));
        assertTrue(result.hasFieldErrors("firstname"));
    }
}