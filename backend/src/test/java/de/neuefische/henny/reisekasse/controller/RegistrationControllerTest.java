package de.neuefische.henny.reisekasse.controller;


import de.neuefische.henny.reisekasse.db.UserDb;
import de.neuefische.henny.reisekasse.model.TravelFoundUser;
import de.neuefische.henny.reisekasse.model.dto.AddTravelFundUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "jwt.secretkey=secretkey")
class RegistrationControllerTest {

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
        userDb.save(TravelFoundUser.builder().username("Janice").password(password_1).build());
    }

    private String getRegistrationUrl() { return "http://localhost:" + port + "auth/registration"; }

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
        ResponseEntity<String> response = restTemplate.postForEntity(getRegistrationUrl(), newUser, String.class);

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
        ResponseEntity<String> response = restTemplate.postForEntity(getRegistrationUrl(), newUser, String.class);

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
        ResponseEntity<String> response = restTemplate.postForEntity(getRegistrationUrl(), newUser, String.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }
}