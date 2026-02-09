package fun.kociarnia.bazy_danych_projekt.user;


import fun.kociarnia.bazy_danych_projekt.city.City;
import fun.kociarnia.bazy_danych_projekt.city.CityRepository;
import fun.kociarnia.bazy_danych_projekt.exception.NotFoundException;
import fun.kociarnia.bazy_danych_projekt.exception.WeakPasswordException;
import fun.kociarnia.bazy_danych_projekt.restaurant.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private CityRepository cityRepository;
    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserByIdShouldReturnAUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test_user");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User foundUser = userService.getUserById(1L);
        assertEquals(1L, foundUser.getId());
        assertEquals("test_user", foundUser.getUsername());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void setCityToUserShouldUpdateUser() {
        City city = new City();
        city.setId(1L);
        city.setName("test_city");

        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

        City oldCity = new City();
        oldCity.setName("old_city");

        User existingUser = new User();
        existingUser.setId(2L);
        existingUser.setUsername("test_user");
        existingUser.setCity(oldCity);
        existingUser.setRole(User.Role.CLIENT);

        when(userRepository.findById(2L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User updatedUser = userService.changeUserCity(2L, 1L);
        assertEquals(2L, updatedUser.getId());
        assertEquals("test_city", updatedUser.getCity().getName());

        verify(cityRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(2L);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void setCityToUserWithCityShouldThrowExceptionWhenCityNotFound() {
        when(cityRepository.findById(4L)).thenReturn(Optional.empty());

        User toUpdate = new User();
        toUpdate.setId(3L);
        toUpdate.setUsername("updated_user");
        when(userRepository.findById(3L)).thenReturn(Optional.of(toUpdate));

        try {
            userService.changeUserCity(3L, 4L);
        } catch (NotFoundException e) {
            assertEquals("City not found with id : '4'", e.getMessage());
        }

        verify(userRepository, times(1)).findById(anyLong());
        verify(cityRepository, times(1)).findById(4L);
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void changePasswordShouldThrowWhenWeakPassword() {
        try {
            userService.changePassword(9L, "abc");
        } catch (WeakPasswordException e) {
            assertEquals(4, e.getErrors().size());
        }

        verify(userRepository, times(0)).findById(9L);
        verify(userRepository, times(0)).save(any());
    }
}

