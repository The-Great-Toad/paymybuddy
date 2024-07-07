package openclassroom.p6.paymybuddy.service;

import lombok.RequiredArgsConstructor;
import openclassroom.p6.paymybuddy.constante.Messages;
import openclassroom.p6.paymybuddy.constante.Regex;
import openclassroom.p6.paymybuddy.domain.Contact;
import openclassroom.p6.paymybuddy.domain.User;
import openclassroom.p6.paymybuddy.domain.record.UserInfoRequest;
import openclassroom.p6.paymybuddy.domain.record.UserPasswordRequest;
import openclassroom.p6.paymybuddy.domain.record.UserRequest;
import openclassroom.p6.paymybuddy.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final String LOG_ID = "[UserService]";

    private final UserRepository userRepository;

    private final  ContactService contactService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(String email) {

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            logger.info("{} - User found with email: {}", LOG_ID, user.get().getEmail());
            return user.get();
        } else {
            logger.info("{} - User with email {} not found", LOG_ID, email);
            return null;
        }
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User getPrincipal() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public BindingResult validateUserPwdRequest(UserPasswordRequest userPwdRequest, User user, BindingResult bindingResult) {
        String oldPassword = userPwdRequest.oldPassword();
        String newPassword = userPwdRequest.newPassword();
        String confirmPassword = userPwdRequest.confirmPassword();
        Pattern passwordRegex = Pattern.compile(Regex.PASSWORD);

        if (StringUtils.isNotBlank(oldPassword) || StringUtils.isNotBlank(newPassword) || StringUtils.isNotBlank(confirmPassword)) {

            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                bindingResult.addError(new FieldError("userPasswordRequest","oldPassword", Messages.PASSWORD_INVALID ));
                logger.error("{} - Old Password do not match current user password", LOG_ID);
            }

            if (newPassword.isBlank()) {
                return bindingResult;
            }

            if (!passwordRegex.matcher(newPassword).matches()) {
                bindingResult.addError(new FieldError("userPasswordRequest","newPassword", Messages.PASSWORD_POLICY));
                logger.error("{} - New Password do not respect password policy", LOG_ID);
            }

            if (!newPassword.equals(confirmPassword)) {
                bindingResult.addError(new FieldError("userRequest","confirmPassword", Messages.PASSWORD_NOT_MATCH ));
                logger.error("{} - Confirm Password do not match new password", LOG_ID);
            }
        }
        return bindingResult;
    }

    public boolean saveUserInfoRequest(UserInfoRequest userInfoRequest, User user) {
        boolean isLastnameModified = !userInfoRequest.lastname().equals(user.getLastname());
        boolean isFirstnameModified = !userInfoRequest.firstname().equals(user.getFirstname());

        if (isLastnameModified) {
            user.setLastname(userInfoRequest.lastname());
        }
        if (isFirstnameModified) {
            user.setFirstname(userInfoRequest.firstname());
        }
        if (isLastnameModified || isFirstnameModified) {
            userRepository.save(user);
            logger.info("{} - User information updated", LOG_ID);
            return true;
        } else {
            logger.info("{} - User information have not changed", LOG_ID);
            return false;
        }
    }

    public void saveUserPwdRequest(UserPasswordRequest userPwdRequest, User user) {
        user.setPassword(passwordEncoder.encode(userPwdRequest.newPassword()));
        userRepository.save(user);
        logger.info("{} - User password updated", LOG_ID);
    }

    public User registerNewUser(UserRequest userRequest) {
        if (emailExits(userRequest.email())) {
            logger.info("{} - User with email {} already exists", LOG_ID, userRequest.email());
            return null;
        }

        // save new user email as contact
        Contact registeredUserAsContact = contactService.saveContact(userRequest.email());
        logger.info("{} - Contact created: {}", LOG_ID, registeredUserAsContact.getEmail());

        // save new user
        User registeredUser = userRepository.save(User.builder()
                .lastname(userRequest.lastname())
                .firstname(userRequest.firstname())
                .email(userRequest.email())
                .password(passwordEncoder.encode(userRequest.password()))
                .balance(5000)
                .role(User.Role.USER)
                .build());
        logger.info("{} - user registered: {}", LOG_ID, registeredUser.getEmail());

        return registeredUser;
    }

    private boolean emailExits(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public BindingResult validateWithdrawal(double balance, double withdrawal, BindingResult bindingResult) {

        if (withdrawal > balance) {
            bindingResult.addError(new FieldError("amountRequest", "amount", Messages.ACCOUNT_INSUFFICIENT_FUNDS));
        }
        return bindingResult;
    }

    public void withdraw(User user, double withdrawal) {
        double balance = user.getBalance();
        logger.info("{} - Withdrawing: ${} from account balance: ${}", LOG_ID, withdrawal, balance);
        user.setBalance(balance - withdrawal);
        logger.info("{} - Account balance after withdraw: ${}", LOG_ID, user.getBalance());
        userRepository.save(user);
    }

    public void deposit(User user, double deposit) {
        double balance = user.getBalance();
        logger.info("{} - Depositing: ${} to account balance: ${}", LOG_ID, deposit, balance);
        user.setBalance(balance + deposit);
        logger.info("{} - Account balance after deposit: ${}", LOG_ID, user.getBalance());
        userRepository.save(user);
    }

    public void logoutUser() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    public BindingResult verifyUserInfoRequest(UserInfoRequest userInfoRequest, BindingResult bindingResult) {
        String lastname = userInfoRequest.lastname();
        String firstname = userInfoRequest.firstname();
        Pattern test = Pattern.compile(Regex.NAME);

        if (Objects.nonNull(lastname) && !test.matcher(lastname).matches()) {
            bindingResult.addError(new FieldError(
                    "userInfoRequest",
                    "lastname",
                    Messages.ALPHA_CHAR_ONLY));
        }
        if (Objects.nonNull(firstname) && !test.matcher(firstname).find()) {
            bindingResult.addError(new FieldError(
                    "userInfoRequest",
                    "firstname",
                    Messages.ALPHA_CHAR_ONLY));
        }
        return bindingResult;
    }
}
