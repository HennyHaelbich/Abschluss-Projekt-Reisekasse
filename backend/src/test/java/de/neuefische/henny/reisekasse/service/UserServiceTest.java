package de.neuefische.henny.reisekasse.service;

import de.neuefische.henny.reisekasse.db.UserDb;
import de.neuefische.henny.reisekasse.model.TravelFoundUser;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class UserServiceTest {
    final UserDb userDb = mock(UserDb.class);
    final UserService userService = new UserService(userDb);


    @Test
    void findUserByIdTestWithExistingUserIdShouldGiveBackOptionOfUser(){
        // Given
        String userId = "Sven";
        TravelFoundUser expectedUser = TravelFoundUser.builder().username(userId).password("SvensPassword").build();
        Optional<TravelFoundUser> optionalExpectedUser = Optional.of(expectedUser);
        when(userDb.findById(userId)).thenReturn(optionalExpectedUser);

        // When
        Optional<TravelFoundUser> resultUser = userService.getUserById(userId);

        // Then
        assertThat(resultUser, is(optionalExpectedUser));
    }

    @Test
    void findUserByIdTestWithNotExistingUserIdShouldGiveBackEmptyOption(){
        // Given
        String userId = "NotExistingUserId";
        when(userDb.findById("NotExistingUserId")).thenReturn(Optional.empty());

        // When
        Optional<TravelFoundUser> resultUser = userService.getUserById(userId);

        // Then
        assertThat(resultUser, is(Optional.empty()));
    }

}