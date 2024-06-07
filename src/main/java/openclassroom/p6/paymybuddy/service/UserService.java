package openclassroom.p6.paymybuddy.service;

import lombok.RequiredArgsConstructor;
import openclassroom.p6.paymybuddy.constante.Messages;
import openclassroom.p6.paymybuddy.constante.Regex;
import openclassroom.p6.paymybuddy.domain.Account;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final String LOG_ID = "[UserService]";

    private final UserRepository userRepository;

    private final  ContactService contactService;

    private final AccountService accountService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
        contactService.deleteContact(user.getEmail());
    }

    public User getPrincipal() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public void removeContactFromUserContacts(User user, Contact contactToRemove) {

//        boolean isContactInUserContactList = user.getContacts().contains(contactToRemove);
//        if (isContactInUserContactList) {
//            logger.info("{} - Before removal - user contact list size: {}", LOG_ID, user.getContacts().size());
//            user.getContacts().remove(contactToRemove);
//            logger.info("{} - After removal - user contact list size: {}", LOG_ID, user.getContacts().size());
//            userRepository.save(user);
//        } else {
//            logger.error("{} - Contact not in User Contacts", LOG_ID);
//        }

        // todo: ask mentor why contact doesn't match from user's contacts list ?????

        List<Contact> contactList = new ArrayList<>();
        user.getContacts().forEach(contact -> {
            if (contact.getEmail().equals(contactToRemove.getEmail())) {
                logger.info("{} - Contact is in user's contact list", LOG_ID);
                contactList.add(contact);
            }
        });

        if (contactList.isEmpty()) {
            logger.error("{} - Contact not in User Contacts", LOG_ID);
        } else {
            logger.info("{} - removing contact '{}' from user's contacts list", LOG_ID, contactList.get(0).getEmail());
            user.getContacts().remove(contactList.get(0));
            userRepository.save(user);
        }
    }

    public BindingResult validateUserPwdRequest(UserPasswordRequest userPwdRequest, User user, BindingResult bindingResult) {
        Pattern passwordRegex = Pattern.compile(Regex.PASSWORD);
        String oldPassword = userPwdRequest.oldPassword();
        String newPassword = userPwdRequest.newPassword();
        String confirmPassword = userPwdRequest.confirmPassword();

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

        int lastAccountId = 10;
        Account newAccount = accountService.saveAccount(Account.builder()
                .id(++lastAccountId)
                .available_balance(5000)
                .build());
        logger.info("{} - New account created: {}", LOG_ID, newAccount.getId());

        Contact registeredUserAsContact = contactService.saveContact(userRequest.email());
        logger.info("{} - Contact created: {}", LOG_ID, registeredUserAsContact.getEmail());

        User registeredUser = userRepository.save(User.builder()
                .lastname(userRequest.lastname())
                .firstname(userRequest.firstname())
                .email(userRequest.email())
                .password(passwordEncoder.encode(userRequest.password()))
                .account(newAccount)
                .role(User.Role.USER)
                .build());
        logger.info("{} - user registered: {}", LOG_ID, registeredUser.getEmail());

        return registeredUser;
    }

    private boolean emailExits(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
