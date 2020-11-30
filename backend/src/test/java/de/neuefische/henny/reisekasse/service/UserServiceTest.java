package de.neuefische.henny.reisekasse.service;

import de.neuefische.henny.reisekasse.db.UserDb;
import de.neuefische.henny.reisekasse.model.TravelFoundUser;
import de.neuefische.henny.reisekasse.model.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserServiceTest {
    final UserDb userDb = mock(UserDb.class);
    final UserService userService = new UserService(userDb);


    @Test
    void findUserByIdTestWithExistingUserIdShouldGiveBackOptionOfUser() {
        // Given
        String userId = "sven.s@web.de";
        TravelFoundUser userFromDb = TravelFoundUser.builder().username(userId).firstName("Sven").lastName("Seifert").password("passwort").build();
        Optional<UserDto> expectedUser = Optional.of(UserDto.builder().username(userId).firstName("Sven").lastName("Seifert").build());

        when(userDb.findById(userId)).thenReturn(Optional.of(userFromDb));

        // When
        Optional<UserDto> resultUser = userService.getUserById(userId);

        // Then
        assertThat(resultUser, is(expectedUser));
    }

    @Test
    void findUserByIdTestWithNotExistingUserIdShouldGiveBackEmptyOption() {
        // Given
        String userId = "NotExistingUserId";
        when(userDb.findById("NotExistingUserId")).thenReturn(Optional.empty());

        // When
        Optional<UserDto> resultUser = userService.getUserById(userId);

        // Then
        assertThat(resultUser, is(Optional.empty()));
    }

    @ParameterizedTest(name = "password {0} should be {1}")
    @CsvSource({
            "aB12, false",
            "adfDFDS234324, true"
    })
    public void testPasswordIsLongEnough(String password, boolean expected){
        // When
        boolean response = userService.passwordIsLongEnough(password);

        // Than
        assertEquals(response, expected);
    }

    @ParameterizedTest(name = "password {0} should be {1}")
    @CsvSource({
            "aBe22rwerwe12, true",
            "dfsdkfDSlsa2, true",
            "dfkdsDFSDFs, false"
    })
    public void testPasswordContainsNumbers(String password, boolean expected){
        // When
        boolean response = userService.containsNumbers(password);

        // Than
        assertEquals(response, expected);
    }

    @ParameterizedTest(name = "password {0} should be {1}")
    @CsvSource({
            "abe22rwerwe12, false",
            "dfsdkfDslsa2, true",
            "dfkdsDFSDFs, true"
    })
    public void testPasswordContainsUpperCase(String password, boolean expected){
        // When
        boolean response = userService.containsUpperCase(password);

        // Than
        assertEquals(response, expected);
    }

    @ParameterizedTest(name = "password {0} should be {1}")
    @CsvSource({
            "DFS22DFER12, false",
            "DFSDFfDsDFE2, true",
            "dfkdsDFSDFs, true"
    })
    public void testPasswordContainsLowerCase(String password, boolean expected){
        // When
        boolean response = userService.containsLowerCase(password);

        // Than
        assertEquals(response, expected);
    }

    @ParameterizedTest(name = "password {0} should be {1}")
    @CsvSource({
            "DFS22DFER12, false",
            "dG87, false",
            "dfkdsdfsd453, false",
            "dsfsDFEdedQE, false",
            "df3423dSDFk, true"
    })
    public void testPasswordIsValid(String password, boolean expected){
        // When
        boolean response = userService.passwordIsValid(password);

        // Than
        assertEquals(response, expected);
    }


}