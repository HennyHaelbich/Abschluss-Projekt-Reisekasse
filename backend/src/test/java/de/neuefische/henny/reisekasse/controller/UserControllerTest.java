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
        assertThat(response.getBody(), is(UserDto.builder().username(username).build()));
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

}