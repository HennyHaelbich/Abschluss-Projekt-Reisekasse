package de.neuefische.henny.reisekasse.service;

import de.neuefische.henny.reisekasse.db.UserDb;
import de.neuefische.henny.reisekasse.model.TravelFoundUser;
import de.neuefische.henny.reisekasse.model.dto.AddTravelFundUserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    void findUserByIdTestWithNotExistingUserIdShouldGiveBackEmptyOption() {
        // Given
        String userId = "NotExistingUserId";
        when(userDb.findById("NotExistingUserId")).thenReturn(Optional.empty());

        // When
        Optional<TravelFoundUser> resultUser = userService.getUserById(userId);

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