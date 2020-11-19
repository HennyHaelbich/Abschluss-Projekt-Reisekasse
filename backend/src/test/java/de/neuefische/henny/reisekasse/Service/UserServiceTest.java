package de.neuefische.henny.reisekasse.Service;

import de.neuefische.henny.reisekasse.Db.UserDb;
import de.neuefische.henny.reisekasse.Model.User;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class UserServiceTest {
    final UserDb userDb = mock(UserDb.class);
    final UserService userService = new UserService(userDb);


    @Test
    void findUserByIdTestWithExistingId(){
        // Given
        String userId = "Sven";
        User user = User.builder().username("Sven").password("SvensPassword").build();
        Optional<User> expectedUser = Optional.of(user);
        when(userDb.findById("Sven")).thenReturn(expectedUser);

        // When
        Optional<User> resultUser = userService.getUserById(userId);

        // Then
        assertThat(resultUser, is(expectedUser));
    }

    @Test
    void findUserByIdTestWithNotExistingId(){
        // Given
        String userId = "NotExistingUserId";
        when(userDb.findById("NotExistingUserId")).thenReturn(Optional.empty());

        // When
        Optional<User> resultUser = userService.getUserById(userId);

        // Then
        assertThat(resultUser, is(Optional.empty()));
    }

}