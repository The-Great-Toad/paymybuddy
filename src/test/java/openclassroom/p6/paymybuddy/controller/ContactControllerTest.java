package openclassroom.p6.paymybuddy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import openclassroom.p6.paymybuddy.constante.Messages;
import openclassroom.p6.paymybuddy.domain.User;
import openclassroom.p6.paymybuddy.domain.record.ContactRequest;
import openclassroom.p6.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = userService.getUser("test@test.com");
    }

    @Test
    void getContactViewTest() throws Exception {
        mockMvc.perform(get("/contacts")
                .with(user(user)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(view().name("contact"))
                .andExpect(model().size(4))
                .andExpect(model().attributeExists("contacts"))
                .andExpect(model().attributeExists("contactRequest"))
                .andExpect(model().attribute("contactRequest", new ContactRequest("")))
                .andExpect(model().attributeExists("contactUnknown"))
                .andExpect(model().attribute("contactUnknown", false))
                .andExpect(model().attributeExists("breadcrumb"))
                .andExpect(model().attribute("breadcrumb", "Contact"))
                .andExpect(content().string(containsString("Add New Connection")))
                .andExpect(content().string(containsString("Contacts")));
    }

    @Test
    void addContactTest_success() throws Exception {
        String newContact = "luc@mail.com";

        mockMvc.perform(post("/contacts")
                        .with(user(user))
                        .queryParam("contactRequest", newContact))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(view().name("contact"))
                .andExpect(model().size(5))
                .andExpect(model().attributeExists("success"))
                .andExpect(model().attribute("success", Messages.getContactAddedSuccess(newContact)))
                .andExpect(content().string(containsString(Messages.getContactAddedSuccess(newContact))));
    }

    @Test
    void addContactTest_failureInvalidEmail() throws Exception {
        String newContact = "toto";

        mockMvc.perform(post("/contacts")
                        .with(user(user))
                        .queryParam("contactRequest", newContact))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(view().name("contact"))
                .andExpect(model().size(3))
                .andExpect(content().string(containsString(Messages.EMAIL_INVALID)));
    }

    @Test
    void addContactTest_failureEmailNotFound() throws Exception {
        String newContact = "toto@mail.com";

        mockMvc.perform(post("/contacts")
                        .with(user(user))
                        .queryParam("contactRequest", newContact))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(view().name("contact"))
                .andExpect(model().size(4))
                .andExpect(content().string(containsString("Contact not found...")));
    }

    @Test
    void removeContactTest_success() throws Exception {
        String newContact = "luc@mail.com";

        mockMvc.perform(post("/contacts")
                        .with(user(user))
                        .queryParam("contactRequest", newContact))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        mockMvc.perform(get("/contacts/remove")
                        .with(user(user))
                        .queryParam("email", newContact))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(view().name("redirect:/contacts"))
                .andExpect(model().size(0))
                .andExpect(flash().attributeCount(1))
                .andExpect(flash().attribute("success", Messages.getContactRemovalSuccess(newContact)));
    }
}