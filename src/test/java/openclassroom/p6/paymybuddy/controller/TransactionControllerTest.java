package openclassroom.p6.paymybuddy.controller;

import openclassroom.p6.paymybuddy.constante.Messages;
import openclassroom.p6.paymybuddy.domain.User;
import openclassroom.p6.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = userService.getUser("test@test.com");
    }

    @Test
    void getTransactionViewTest() throws Exception {
        mockMvc.perform(get("/transactions")
                        .with(user(user)))
                .andExpect(status().isOk())
                .andExpect(view().name("transaction"))
                .andExpect(model().size(10))
                .andExpect(model().attribute("user", user))
                .andExpect(model().attribute("breadcrumb", "Transfer"))
                .andExpect(model().attributeExists("transactionRequest"))
                .andExpect(model().attributeExists("contacts"))
                .andExpect(view().name("transaction"))
                .andExpect(content().string(containsString("Send Money")))
                .andExpect(content().string(containsString("My Transactions")));
    }

    @Test
    void saveTransactionTest_success() throws Exception {
        MultiValueMap<String, String> params = new HttpHeaders();
        params.add("receiver", "receiver@test.com");
        params.add("description", "free money");
        params.add("amount", "2000");

        mockMvc.perform(post("/transactions")
                        .with(user(user))
                        .queryParams(params))
                .andExpect(status().isOk())
                .andExpect(view().name("transaction"))
                .andExpect(model().size(11))
                .andExpect(model().attribute("user", user))
                .andExpect(model().attribute("success", Messages.TRANSFER_SUCCESS))
                .andExpect(content().string(containsString((String) Messages.TRANSFER_SUCCESS)));
    }

    @Test
    void saveTransactionTest_failureInvalidAmount() throws Exception {
        MultiValueMap<String, String> params = new HttpHeaders();
        params.add("receiver", "receiver@test.com");
        params.add("description", "negative amount");
        params.add("amount", "-10");

        mockMvc.perform(post("/transactions")
                        .with(user(user))
                        .queryParams(params))
                .andExpect(status().isOk())
                .andExpect(view().name("transaction"))
                .andExpect(model().size(10))
                .andExpect(model().attribute("user", user))
                .andExpect(model().attributeHasFieldErrorCode("transactionRequest", "amount", "Min"))
                .andExpect(content().string(containsString(Messages.ACCOUNT_MIN_DEPOSIT)));
    }

}