package de.neuefische.henny.reisekasse.controller;

import de.neuefische.henny.reisekasse.db.UserDb;
import de.neuefische.henny.reisekasse.model.TravelFoundUser;
import de.neuefische.henny.reisekasse.model.dto.LoginDto;
import de.neuefische.henny.reisekasse.model.dto.UserDto;
import de.neuefische.henny.reisekasse.model.dto.AddTravelFundUserDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "jwt.secretkey=secretkey")
class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserDb userDb;

    @BeforeEach
    public void setupDb() {
        userDb.deleteAll();

        String password_1 = new BCryptPasswordEncoder().encode("superPassword123");
        String password_2 = new BCryptPasswordEncoder().encode("superPassword456");

        userDb.save(TravelFoundUser.builder().username("Henny").password(password_1).build());
        userDb.save(TravelFoundUser.builder().username("Janice").password(password_2).build());
    }

    private String getUserUrl() {
        return "http://localhost:" + port + "/api/users";
    }

    private String login() {
        ResponseEntity<String> response = restTemplate.postForEntity( "http://localhost:" + port + "auth/login",
                new LoginDto( "Henny", "superPassword123"), String.class);

        return response.getBody();
    }

    private <T> HttpEntity<T> getValidAuthorizationEntity(T data) {
        String token = login();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(data, headers);
    }

    @Test
    public void testGetUserWithExistingUsernameShouldReturnUsername() {
        // Given
        String username = "Janice";

        // When
        HttpEntity<Void> entity = getValidAuthorizationEntity(null);
        ResponseEntity<UserDto> response = restTemplate.exchange(getUserUrl() + "/" + username, HttpMethod.GET, entity, UserDto.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(new UserDto(username)));
    }

    @Test
    public void testGetUserWithNotExistingUsernameShouldThrowNotFoundException() {
        // Given
        String username = "UnknownUserName";

        // When
        HttpEntity<Void> entity = getValidAuthorizationEntity(null);
        ResponseEntity<UserDto> response = restTemplate.exchange(getUserUrl() + "/" + username, HttpMethod.GET, entity, UserDto.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    void testRegisterNewUserShouldReturnUsername() {
        // Given
        AddTravelFundUserDto newUser = AddTravelFundUserDto.builder()
                .username("max.mustermann@web.de")
                .password("34dkfdSERE89")
                .firstName("Max")
                .lastName("Mustermann")
                .build();

        // When
        HttpEntity<AddTravelFundUserDto> entity = getValidAuthorizationEntity(newUser);
        ResponseEntity<String> response = restTemplate.exchange(getUserUrl(), HttpMethod.POST, entity, String.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is("Max"));
    }

    @Test
    void testRegisterNewUserShouldThrowBadRequestWhenPasswordNotValid() {
        // Given
        AddTravelFundUserDto newUser = AddTravelFundUserDto.builder()
                .username("max.mustermann@web.de")
                .password("aaaa")
                .firstName("Max")
                .lastName("Mustermann")
                .build();

        // When
        HttpEntity<AddTravelFundUserDto> entity = getValidAuthorizationEntity(newUser);
        ResponseEntity<String> response = restTemplate.exchange(getUserUrl(), HttpMethod.POST, entity, String.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void testRegisterNewUserShouldThrowBadRequestWhenUsernameIsAlreadyInDatabase() {
        // Given
        AddTravelFundUserDto newUser = AddTravelFundUserDto.builder()
                .username("Janice")
                .password("34dkfdSERE89")
                .firstName("Max")
                .lastName("Mustermann")
                .build();

        // When
        HttpEntity<AddTravelFundUserDto> entity = getValidAuthorizationEntity(newUser);
        ResponseEntity<String> response = restTemplate.exchange(getUserUrl(), HttpMethod.POST, entity, String.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }
}