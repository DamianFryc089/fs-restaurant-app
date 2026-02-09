package fun.kociarnia.bazy_danych_projekt.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import fun.kociarnia.bazy_danych_projekt.user.dto.CreateUserDTO;
import fun.kociarnia.bazy_danych_projekt.user.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("local")
@AutoConfigureMockMvc(addFilters = false)
class OtherTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "clientUser", roles = {"CLIENT"})
    void shouldPreventClientFromChangingOtherUserData() throws Exception {
        UserDTO dto = new UserDTO();
        dto.setUsername("hacker");
        dto.setEmail("hacker@test.com");

        mockMvc.perform(put("/users/admin/1")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        mockMvc.perform(get("/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Resource not found"));
    }

    @Test
    void shouldRejectIncompleteCreateUserDTO() throws Exception {
        CreateUserDTO dto = new CreateUserDTO();
        dto.setUsername("");
        dto.setEmail("test@test.com");

        mockMvc.perform(post("/users")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }
}


