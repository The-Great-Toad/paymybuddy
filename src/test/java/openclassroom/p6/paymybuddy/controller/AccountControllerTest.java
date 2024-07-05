package openclassroom.p6.paymybuddy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import openclassroom.p6.paymybuddy.constante.Messages;
import openclassroom.p6.paymybuddy.domain.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
//@WithMockUser(value = "test@test.com", password = "test", roles = "USER")
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private static User user;

    @BeforeAll
    static void setUp() {
        user = User.builder()
                .firstname("test")
                .lastname("test")
                .email("test@test.com")
                .password("test")
                .balance(2000)
                .role(User.Role.USER)
                .build();
    }

    @Test
    void withdrawTest_success() throws Exception {
        mockMvc.perform(post("/account/withdraw")
                        .with(user(user))
                        .queryParam("amount", "10"))
//                .andDo(print())
//                .andExpect(redirectedUrl("/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().size(0))
                .andExpect(flash().attributeCount(1))
                .andExpect(flash().attribute("success", Messages.WITHDRAW_SUCCESS))
                .andExpect(view().name("redirect:/profile"));
    }

    @Test
    void withdrawTest_failureInsufficientFund() throws Exception {
        mockMvc.perform(post("/account/withdraw")
                        .with(user(user))
                        .queryParam("amount", "1000000"))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().size(5))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("userInfoRequest"))
                .andExpect(model().attributeExists("userPasswordRequest"))
                .andExpect(model().attributeExists("breadcrumb"))
                .andExpect(flash().attributeCount(0))
                .andExpect(view().name("profile"))
                .andExpect(content().string(containsString("invalid-feedback")))
                .andExpect(content().string(containsString(Messages.ACCOUNT_INSUFFICIENT_FUNDS)));
    }

    @Test
    void depositTest_success() throws Exception {
        mockMvc.perform(post("/account/deposit")
                        .with(user(user))
                        .queryParam("amount", "100"))
//                .andDo(print())
                .andExpect(redirectedUrl("/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().size(0))
                .andExpect(flash().attributeCount(1))
                .andExpect(flash().attribute("success", Messages.DEPOSIT_SUCCESS))
                .andExpect(view().name("redirect:/profile"));
    }

    @Test
    void depositTest_failureAmountNegative() throws Exception {
        mockMvc.perform(post("/account/deposit")
                        .with(user(user))
                        .queryParam("amount", "-10"))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().size(5))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("userInfoRequest"))
                .andExpect(model().attributeExists("userPasswordRequest"))
                .andExpect(model().attributeExists("breadcrumb"))
                .andExpect(flash().attributeCount(0))
                .andExpect(view().name("profile"))
                .andExpect(content().string(containsString("invalid-feedback")))
                .andExpect(content().string(containsString(Messages.ACCOUNT_MIN_DEPOSIT)));
    }
}