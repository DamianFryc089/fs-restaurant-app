package fun.kociarnia.bazy_danych_projekt.user;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFindUser() {
        User user = new User();
        user.setUsername("test_user");
        user.setEmail("test@example.com");
        user.setPassword("testtest");
        user.setRole(User.Role.CLIENT);

        userRepository.save(user);

        List<User> users = userRepository.findAll();
        assertEquals(1, users.size());
        assertEquals("test_user", users.getFirst().getUsername());
    }

    @Test
    void shouldSaveAndFindEmployees() {
        List<User> users = List.of(
                createUser("usera", User.Role.CLIENT),
                createUser("userb", User.Role.CLIENT),
                createUser("userc", User.Role.EMPLOYEE),
                createUser("userd", User.Role.ADMIN),
                createUser("usere", User.Role.EMPLOYEE),
                createUser("userf", User.Role.CLIENT)
        );
        userRepository.saveAll(users);

        List<User> employees = userRepository.findByRole(User.Role.EMPLOYEE);

        assertEquals(2, employees.size());
        assertEquals("userc", employees.get(0).getUsername());
        assertEquals("usere", employees.get(1).getUsername());
    }

    private User createUser(String base, User.Role role) {
        User u = new User();
        u.setUsername(base);
        u.setEmail(base + "@example.com");
        u.setPassword("pass" + base);
        u.setRole(role);
        return u;
    }
}

