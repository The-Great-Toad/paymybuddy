package openclassroom.p6.paymybuddy.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import openclassroom.p6.paymybuddy.domain.Contact;
import openclassroom.p6.paymybuddy.domain.User;
import openclassroom.p6.paymybuddy.domain.record.ContactRequest;
import openclassroom.p6.paymybuddy.service.ContactService;
import openclassroom.p6.paymybuddy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);
    private final String LOG_ID = "[ContactController]";

    private final ContactService contactService;
    private final UserService userService;


    @GetMapping
    public String contactView(
            Model model,
            Authentication authentication)
    {
        logger.info("{} - principal: {}", LOG_ID, authentication.getPrincipal());
        User user = (User) authentication.getPrincipal();
        List<Contact> contacts =  contactService.getUserContacts(user.getId());
        logger.info("{} - contacts: {}", LOG_ID, contacts.size());

        model.addAttribute("contacts", contacts);
        model.addAttribute("contactRequest", new ContactRequest(""));
        model.addAttribute("contactUnknown", false);

        return "contact";
    }

    @PostMapping
    public String addContactToUserContactList(
            Model model,
            Authentication authentication,
            @Valid ContactRequest contactRequest,
            BindingResult bindingResult)
    {
        User user = (User) authentication.getPrincipal();
        logger.info("{} - contact request: {}", LOG_ID, contactRequest);

        // Validate contact Request - Check for errors
        if (bindingResult.hasErrors()) {
            logger.error("{} - bindingResult errors: {}", LOG_ID, bindingResult.hasFieldErrors("email"));
            Iterable<Contact> contacts =  contactService.getUserContacts(user.getId());
            model.addAttribute("contacts", contacts);
            return "contact";
        }

        // Check if contact exists in DB
        Contact newContact = contactService.validateContactRequest(contactRequest);
        if (newContact != null) {
            // Add contact to user's list
            user.getContacts().add(newContact);
            userService.saveUser(user);
            logger.info("{} - Contact '{}' added to user contact list", LOG_ID, newContact.getEmail());

            model.addAttribute("contactSuccess", true);
            model.addAttribute("contactRequest", new ContactRequest(""));
            model.addAttribute("contactUnknown", false);
        } else {
            // Contact isn't found
            logger.error("{} - Contact '{}' not found", LOG_ID, contactRequest.email());
            model.addAttribute("contactUnknown", true);
        }

        Iterable<Contact> contacts =  contactService.getUserContacts(user.getId());
        model.addAttribute("contacts", contacts);

        return "contact";
    }

    @GetMapping("/remove-contact")
    public String contactDelete(
            Model model,
            Authentication authentication,
            @RequestParam String email)
    {
        User user = (User) authentication.getPrincipal();
        logger.info("{} - contact to remove: {}", LOG_ID, email);

        // Retrieve Contact
        Contact contactToRemove = contactService.getContact(email).get();

        // Remove it from user's list
        userService.removeContactFromUserContacts(user, contactToRemove);
        List<Contact> contacts =  contactService.getUserContacts(user.getId());

        model.addAttribute("contacts", contacts);
        model.addAttribute("contactRequest", new ContactRequest(""));
        model.addAttribute("contactUnknown", false);

        return "redirect:/contacts";
    }

}
