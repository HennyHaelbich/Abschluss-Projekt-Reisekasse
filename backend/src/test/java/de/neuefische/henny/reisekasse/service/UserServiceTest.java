package de.neuefische.henny.reisekasse.service;

import de.neuefische.henny.reisekasse.db.UserDb;
import de.neuefische.henny.reisekasse.model.User;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {
    final UserDb userDb = mock(UserDb.class);
    final UserService userService = new UserService(userDb);


    @Test
    void findUserByIdTestWithExistingUserIdShouldGiveBackOptionOfUser() {
        // Given
        String userId = "Sven";
        User expectedUser = User.builder().username(userId).password("SvensPassword").build();
        Optional<User> optionalExpectedUser = Optional.of(expectedUser);
        when(userDb.findById(userId)).thenReturn(optionalExpectedUser);

        // When
        Optional<User> resultUser = userService.getUserById(userId);

        // Then
        assertThat(resultUser, is(optionalExpectedUser));
    }

    @Test
    void findUserByIdTestWithNotExistingUserIdShouldGiveBackEmptyOption() {
        // Given
        String userId = "NotExistingUserId";
        when(userDb.findById("NotExistingUserId")).thenReturn(Optional.empty());

        // When
        Optional<User> resultUser = userService.getUserById(userId);

        // Then
        assertThat(resultUser, is(Optional.empty()));
    }

}