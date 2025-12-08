package fun.kociarnia.bazy_danych_projekt.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import fun.kociarnia.bazy_danych_projekt.user.dto.CreateUserDTO;
import fun.kociarnia.bazy_danych_projekt.user.dto.UserDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("local")
@Transactional
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class Config {
        @Bean
        UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }

    @Autowired
    private UserService userService;

    @Test
    void shouldCreateUser() throws Exception {
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setUsername("test1");
        createUserDTO.setEmail("test@test.com");
        createUserDTO.setPassword("test123");

        User createdUser = new User();
        createdUser.setId(1L);
        createdUser.setUsername("test1");
        createdUser.setEmail("test@test.com");
        createdUser.setRole(User.Role.CLIENT);

        when(userService.createUser(any(User.class))).thenReturn(createdUser);

        mockMvc.perform(post("/users")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(new ObjectMapper().writeValueAsString(createUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("test1"))
                .andExpect(jsonPath("$.email").value("test@test.com"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    void shouldUpdateUserByAdmin() throws Exception {
        UserDTO dto = new UserDTO();
        dto.setId(5L);
        dto.setUsername("nowy1");
        dto.setEmail("nowy@test.com");
        dto.setRole("CLIENT");
        dto.setStatus("ACTIVE");
        dto.setCityName("Kraków");
        dto.setRestaurantId(99L);

        User updated = new User();
        updated.setId(5L);
        updated.setUsername("zmienony1");
        updated.setEmail("nowyemail@test.com");

        when(userService.updateUserAdmin(Mockito.eq(5L), any(User.class),
                Mockito.eq("Kraków"), Mockito.eq(99L)))
                .thenReturn(updated);

        mockMvc.perform(post("/users/admin/5")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(new ObjectMapper().writeValueAsString(dto))
                        .with(req -> {
                            req.setMethod("PUT");
                            return req;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.username").value("zmienony1"))
                .andExpect(jsonPath("$.email").value("nowyemail@test.com"));
    }

    @Test
    void shouldReturnForbiddenWhenNotAdminOnGetUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().is5xxServerError());
    }
}


