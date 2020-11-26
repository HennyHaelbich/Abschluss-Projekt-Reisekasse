package de.neuefische.henny.reisekasse.controller;

import de.neuefische.henny.reisekasse.db.UserDb;
import de.neuefische.henny.reisekasse.model.TravelFoundUser;
import de.neuefische.henny.reisekasse.model.dto.LoginDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "jwt.secretkey=secretkey")
class LoginControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserDb userDb;

    private final String secretKey = "secretkey";

    @BeforeEach
    public void setUpUserDb() {
        userDb.deleteAll();
        String password = new BCryptPasswordEncoder().encode("superPassword123");
        userDb.save(new TravelFoundUser("henny", password));
    }

    private String getLoginUrl() { return "http://localhost:" + port + "auth/login"; }

    @Test
    public void loginShouldReturnJwtToken() {
        // Given
        LoginDto loginDto = new LoginDto("henny", "superPassword123");

        // When
        ResponseEntity<String> response = restTemplate.postForEntity(getLoginUrl(), loginDto, String.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        String token = response.getBody();
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

        assertThat(claims.getSubject(), is("henny"));
        assertThat(claims.getExpiration().after(new Date()), is(true));
    }

    @Test
    public void loginWithInvalidCredentialsShouldReturnForbidden() {
        // Given
        LoginDto loginDto = new LoginDto("henny", "wrongPassword123");

        // When
        ResponseEntity<String> response = restTemplate.postForEntity(getLoginUrl(), loginDto, String.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }
}

