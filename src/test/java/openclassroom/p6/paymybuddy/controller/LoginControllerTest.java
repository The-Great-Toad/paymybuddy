package openclassroom.p6.paymybuddy.controller;

import openclassroom.p6.paymybuddy.constante.Messages;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest extends ControllerUtils {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getHomeTest() throws Exception {
        mockMvc.perform(get("/")
                        .with(user(user)))
                .andExpect(status().isOk())
                .andExpect(model().size(4))
                .andExpect(model().attribute("user", user))
                .andExpect(model().attribute("breadcrumb", "Home"))
                .andExpect(model().attributeExists("recentTransactions"))
                .andExpect(model().attributeExists("recentContacts"))
                .andExpect(view().name("index"))
                .andExpect(content().string(containsString("Welcome,")))
                .andExpect(content().string(containsString(user.getFirstname())));
    }

    @Test
    void getLoginFormTest() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(content().string(containsString("Pay My Buddy")));
    }

    @Test
    void logoutTest() throws Exception {
        mockMvc.perform(post("/user/logout")
                        .with(user(user)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(view().name("redirect:/login"))
                .andExpect(flash().attribute("logoutSuccess", Messages.LOGOUT_SUCCESS));
    }

    @Test
    void getRegistrationFormTest() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("userRequest"))
                .andExpect(view().name("register"))
                .andExpect(content().string(containsString("Pay My Buddy - Registration")));
    }

    @Test
    void registerTest_success() throws Exception {
        MultiValueMap<String, String> params = new HttpHeaders();
        params.add("lastname", "Sponge");
        params.add("firstname", "Bob");
        params.add("email", "sponge.bob@mail.com");
        params.add("password", "Pa$$w0rd");

        mockMvc.perform(post("/register/save")
                        .queryParams(params))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attribute("registrationSuccess", Messages.REGISTRATION_SUCCESS))
                .andExpect(view().name("redirect:/login"));
    }

    @Test
    void registerTest_failureNotBlank() throws Exception {
        mockMvc.perform(post("/register/save"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("register"))
                .andExpect(model().size(1))
                .andExpect(model().attributeExists("userRequest"))
                .andExpect(model().attributeHasFieldErrorCode("userRequest", "lastname", "NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("userRequest", "firstname", "NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("userRequest", "email", "NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("userRequest", "password", "NotBlank"));
    }

    @Test
    void registerTest_failureEmailUsed() throws Exception {
        MultiValueMap<String, String> params = new HttpHeaders();
        params.add("lastname", "toto");
        params.add("firstname", "toto");
        params.add("email", "test@test.com");
        params.add("password", "Pa$$w0rd");

        mockMvc.perform(post("/register/save")
                        .queryParams(params))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("register"))
                .andExpect(model().size(2))
                .andExpect(model().attributeExists("userRequest"))
                .andExpect(model().attribute("emailUsed", Messages.EMAIL_ALREADY_USED));
    }

    @Test
    void registerTest_failurePasswordPolicy() throws Exception {
        MultiValueMap<String, String> params = new HttpHeaders();
        params.add("lastname", "toto");
        params.add("firstname", "toto");
        params.add("email", "test@test.com");
        params.add("password", "password");

        mockMvc.perform(post("/register/save")
                        .queryParams(params))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("register"))
                .andExpect(model().size(1))
                .andExpect(model().attributeExists("userRequest"))
                .andExpect(model().attributeHasFieldErrorCode("userRequest", "password", "Pattern"));
    }
}