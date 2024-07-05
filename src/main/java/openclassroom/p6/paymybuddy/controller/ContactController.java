package openclassroom.p6.paymybuddy.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import openclassroom.p6.paymybuddy.constante.Messages;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);
    private final String LOG_ID = "[ContactController]";

    private final ContactService contactService;
    private final UserService userService;


    @GetMapping
    public String getContactView(
            Model model,
            Authentication authentication)
    {
        User user = (User) authentication.getPrincipal();
//        User user = userService.getUser("test@test.com");

        model.addAttribute("contacts", contactService.getUserContactList(user.getEmail()));
        model.addAttribute("contactRequest", new ContactRequest(""));
        model.addAttribute("contactUnknown", false);
        model.addAttribute("breadcrumb", "Contact");

        return "contact";
    }

    @PostMapping
    public String addContact(
            Model model,
            Authentication authentication,
            @Valid ContactRequest contactRequest,
            BindingResult bindingResult)
    {
        User user = (User) authentication.getPrincipal();
//        User user = userService.getUser("test@test.com");

        logger.info("{} - contact request: {}", LOG_ID, contactRequest);

        // Validate contact Request - Check for errors
        if (bindingResult.hasErrors()) {
            logger.error("{} - bindingResult errors: {}", LOG_ID, bindingResult.hasFieldErrors("email"));

            model.addAttribute("contacts", contactService.getUserContactList(user.getEmail()));
            model.addAttribute("breadcrumb", "Contact");

            return "contact";
        }

        // Check if contact exists in DB
        Contact newContact = contactService.validateContactRequest(contactRequest);
        if (newContact != null) {
            user.getContacts().add(newContact);
            userService.save(user);
            logger.info("{} - Contact '{}' added to user contact list", LOG_ID, newContact.getEmail());

            model.addAttribute("success", Messages.getContactAddedSuccess(newContact.getEmail()));
            model.addAttribute("contactRequest", new ContactRequest(""));
            model.addAttribute("contactUnknown", false);
        } else {
            logger.error("{} - Contact '{}' not found", LOG_ID, contactRequest.email());
            model.addAttribute("contactUnknown", true);
        }

        model.addAttribute("contacts", contactService.getUserContactList(user.getEmail()));
        model.addAttribute("breadcrumb", "Contact");

        return "contact";
    }

    @GetMapping("/remove")
    public String removeContact(
            Authentication authentication,
            RedirectAttributes redirectAttributes,
            @RequestParam String email)
    {
        logger.info("{} - contact to remove: {}", LOG_ID, email);

        User user = (User) authentication.getPrincipal();
//        User user = userService.getUser("test@test.com");

        // Retrieve contact to remove
        Contact contactToRemove = contactService.getContact(email);
        // Remove Contact
        user.removeContact(contactToRemove);
        // Update user
        userService.save(user);

        redirectAttributes.addFlashAttribute("success", Messages.getContactRemovalSuccess(email));
        return "redirect:/contacts";
    }

}
