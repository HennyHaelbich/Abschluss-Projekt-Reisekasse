package de.neuefische.henny.reisekasse.controller;

import de.neuefische.henny.reisekasse.db.EventDb;
import de.neuefische.henny.reisekasse.db.UserDb;
import de.neuefische.henny.reisekasse.model.TravelFoundUser;
import de.neuefische.henny.reisekasse.model.dto.AddEventDto;
import de.neuefische.henny.reisekasse.model.dto.LoginDto;
import de.neuefische.henny.reisekasse.model.dto.UserDto;
import de.neuefische.henny.reisekasse.model.Event;
import de.neuefische.henny.reisekasse.model.EventMember;
import de.neuefische.henny.reisekasse.utils.IdUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.when;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "jwt.secretkey=secretkey")
class EventControllerTest {

    @LocalServerPort
    private int port;

    @MockBean
    private IdUtils mockedIdUtils;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EventDb eventDb;

    @Autowired
    private UserDb userDb;

    @BeforeEach
    public void setupDb(){
        eventDb.deleteAll();
        eventDb.saveAll(List.of(
                Event.builder().id("id_1").title("Schwedenreise")
                        .members(List.of(
                                new EventMember("Janice", 0.0),
                                new EventMember("Manu", 0)))
                        .expenditures(List.of()).build(),
                Event.builder().id("id_2").title("Norwegen 2020")
                        .members(List.of(
                                new EventMember("Julius", 0.0),
                                new EventMember("Henny", 0),
                                new EventMember("Manu", 0)))
                        .expenditures(List.of()).build()
        ));

        userDb.deleteAll();
        String password = new BCryptPasswordEncoder().encode("superPassword123");
        userDb.save(new TravelFoundUser("henny", password));
    }

    private String getEventUrl() {
        return "http://localhost:" + port + "/api/events";
    }

    private String login() {
        ResponseEntity<String> response = restTemplate.postForEntity( "http://localhost:" + port + "auth/login",
                new LoginDto( "henny", "superPassword123"), String.class);

        return response.getBody();
    }

    private <T> HttpEntity<T> getValidAuthorizationEntity(T data) {
        String token = login();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(data, headers);
    }

    @Test
    void testPostMapping() {
        // Given
        AddEventDto eventToBeAdded = AddEventDto.builder()
                .title("Norwegen 2020")
                .members(List.of(new UserDto("Julius"),
                        new UserDto("Henny")))
                .build();
        when(mockedIdUtils.generateId()).thenReturn("id");

        Event expectedEvent = Event.builder()
                .id("id")
                .title("Norwegen 2020")
                .members(List.of(new EventMember("Julius", 0.0),
                        new EventMember("Henny", 0.0)))
                .expenditures(new ArrayList<>())
                .build();

        // When
        HttpEntity<AddEventDto> entity = getValidAuthorizationEntity(eventToBeAdded);
        ResponseEntity<Event> response = restTemplate.exchange(getEventUrl(), HttpMethod.POST, entity, Event.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(expectedEvent));
    }

    @Test
    void testGetMapping(){
        // Given
        List<Event> eventList = List.of(
                Event.builder().id("id_1").title("Schwedenreise")
                        .members(List.of(
                                new EventMember("Janice", 0.0),
                                new EventMember("Manu", 0)))
                        .expenditures(List.of()).build(),
                Event.builder().id("id_2").title("Norwegen 2020")
                        .members(List.of(
                                new EventMember("Julius", 0.0),
                                new EventMember("Henny", 0),
                                new EventMember("Manu", 0)))
                        .expenditures(List.of()).build());

        // When
        HttpEntity<AddEventDto> entity = getValidAuthorizationEntity(null);
        ResponseEntity<Event[]> response = restTemplate.exchange(getEventUrl(), HttpMethod.GET, entity, Event[].class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(eventList.toArray()));
    }
}