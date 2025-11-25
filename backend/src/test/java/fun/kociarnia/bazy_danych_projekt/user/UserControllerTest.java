package fun.kociarnia.bazy_danych_projekt.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import fun.kociarnia.bazy_danych_projekt.user.dto.CreateUserDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
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
}


