package de.neuefische.henny.reisekasse.controller;

import de.neuefische.henny.reisekasse.db.UserDb;
import de.neuefische.henny.reisekasse.model.User;
import de.neuefische.henny.reisekasse.model.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
        userDb.saveAll(List.of(
                User.builder().username("Malte").build(),
                User.builder().username("Sven").build(),
                User.builder().username("Dennis").build()
        ));
    }

    private String getUserUrl() {
        return "http://localhost:" + port + "/api/users";
    }

    @Test
    public void testGetUserWithExistingUsernameShouldReturnUsername() {
        // Given
        String username = "Malte";

        // When
        ResponseEntity<UserDto> response = restTemplate.getForEntity(getUserUrl() + "/" + username, UserDto.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(new UserDto(username)));

    }

    @Test
    public void testGetUserWithNotExistingUsernameShouldThrowNotFoundException() {
        // Given
        String username = "Henny";

        // When
        ResponseEntity<UserDto> response = restTemplate.getForEntity(getUserUrl() + "/" + username, UserDto.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));

    }
}