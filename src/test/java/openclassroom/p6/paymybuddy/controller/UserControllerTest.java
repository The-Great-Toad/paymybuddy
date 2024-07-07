package openclassroom.p6.paymybuddy.controller;

import openclassroom.p6.paymybuddy.constante.Messages;
import openclassroom.p6.paymybuddy.domain.User;
import openclassroom.p6.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {

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
    void getProfileViewTest() throws Exception {
        mockMvc.perform(get("/profile")
                        .with(user(user)))
                .andExpect(status().isOk())
                .andExpect(model().size(5))
                .andExpect(model().attribute("user", user))
                .andExpect(model().attribute("breadcrumb", "Profile"))
                .andExpect(model().attributeExists("userInfoRequest"))
                .andExpect(model().attributeExists("amountRequest"))
                .andExpect(model().attributeExists("userPasswordRequest"))
                .andExpect(view().name("profile"))
                .andExpect(content().string(containsString("My Information")))
                .andExpect(content().string(containsString("My Account")))
                .andExpect(content().string(containsString("Change My Password")))
                .andExpect(content().string(containsString("Password Policy")));
    }

    @Test
    void saveUserInfoTest_success() throws Exception {
        String lastnameChanged = "Sponge";
        String firstnameChanged = "Bob";

        mockMvc.perform(post("/profile/info")
                        .with(user(user))
                        .queryParam("lastname", lastnameChanged)
                        .queryParam("firstname", firstnameChanged))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"))
                .andExpect(model().size(0))
                .andExpect(flash().attribute("success", Messages.USER_INFO_SUCCESS))
                .andExpect(view().name("redirect:/profile"));
    }

    @Test
    void saveUserInfoTest_failureNotBlank() throws Exception {
        String lastnameChanged = "";
        String firstnameChanged = "";

        mockMvc.perform(post("/profile/info")
                        .with(user(user))
                        .queryParam("lastname", lastnameChanged)
                        .queryParam("firstname", firstnameChanged))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().size(5))
                .andExpect(model().attributeHasFieldErrorCode("userInfoRequest", "lastname", "NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("userInfoRequest", "firstname", "NotBlank"))
                .andExpect(view().name("profile"))
                .andExpect(content().string(containsString(Messages.REQUIRED)));
    }

    @Test
    void saveUserInfoTest_failurePattern() throws Exception {
        String lastnameChanged = "123";
        String firstnameChanged = "123";

        mockMvc.perform(post("/profile/info")
                        .with(user(user))
                        .queryParam("lastname", lastnameChanged)
                        .queryParam("firstname", firstnameChanged))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().size(5))
                .andExpect(model().attributeErrorCount("userInfoRequest", 2))
                .andExpect(model().attributeHasFieldErrors("userInfoRequest", "lastname", "firstname"))
                .andExpect(view().name("profile"))
                .andExpect(content().string(containsString(Messages.ALPHA_CHAR_ONLY)));
    }

    @Test
    void saveUserPasswordTest_success() throws Exception {
        String oldPassword = "test";
        String newPassword = "Password!123";
        String confirmPassword = "Password!123";

        mockMvc.perform(post("/profile/password")
                        .with(user(user))
                        .queryParam("oldPassword", oldPassword)
                        .queryParam("newPassword", newPassword)
                        .queryParam("confirmPassword", confirmPassword))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"))
                .andExpect(model().size(0))
                .andExpect(flash().attribute("success", Messages.PASSWORD_SUCCESS))
                .andExpect(view().name("redirect:/profile"));
    }

    @Test
    void saveUserPasswordTest_failureNotBlank() throws Exception {
        String oldPassword = "";
        String newPassword = "";
        String confirmPassword = "";

        mockMvc.perform(post("/profile/password")
                        .with(user(user))
                        .queryParam("oldPassword", oldPassword)
                        .queryParam("newPassword", newPassword)
                        .queryParam("confirmPassword", confirmPassword))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().size(5))
                .andExpect(model().attributeHasFieldErrorCode("userPasswordRequest", "oldPassword", "NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("userPasswordRequest", "newPassword", "NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("userPasswordRequest", "confirmPassword", "NotBlank"))
                .andExpect(view().name("profile"))
                .andExpect(content().string(containsString(Messages.OLD_PWD_NOT_NULL)))
                .andExpect(content().string(containsString(Messages.NEW_PWD_NOT_NULL)))
                .andExpect(content().string(containsString(Messages.CONFIRM_PWD_NOT_NULL)));
    }

    @Test
    void saveUserPasswordTest_failurePasswordPolicy() throws Exception {
        String oldPassword = "do not match current password";
        String newPassword = "do not match password policy";
        String confirmPassword = "do not match new password";

        mockMvc.perform(post("/profile/password")
                        .with(user(user))
                        .queryParam("oldPassword", oldPassword)
                        .queryParam("newPassword", newPassword)
                        .queryParam("confirmPassword", confirmPassword))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().size(5))
                .andExpect(model().attributeErrorCount("userPasswordRequest", 3))
                .andExpect(model().attributeHasFieldErrors("userPasswordRequest", "oldPassword", "newPassword", "confirmPassword"))
                .andExpect(view().name("profile"))
                .andExpect(content().string(containsString(Messages.PASSWORD_INVALID)))
                .andExpect(content().string(containsString(Messages.PASSWORD_POLICY)))
                .andExpect(content().string(containsString(Messages.PASSWORD_NOT_MATCH)));
    }
}