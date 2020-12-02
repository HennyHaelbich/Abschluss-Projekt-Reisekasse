package de.neuefische.henny.reisekasse.controller;

import de.neuefische.henny.reisekasse.db.EventDb;
import de.neuefische.henny.reisekasse.model.*;
import de.neuefische.henny.reisekasse.db.UserDb;
import de.neuefische.henny.reisekasse.model.dto.*;
import de.neuefische.henny.reisekasse.utils.IdUtils;
import de.neuefische.henny.reisekasse.utils.TimestampUtils;
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

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "jwt.secretkey=secretkey")
class EventControllerTest {

    @LocalServerPort
    private int port;

    @MockBean
    private IdUtils mockedIdUtils;

    @MockBean
    private TimestampUtils mockedtimestampUtils;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EventDb eventDb;

    @Autowired
    private UserDb userDb;

    @BeforeEach
    public void setupDb() {
        eventDb.deleteAll();
        eventDb.saveAll(List.of(
                Event.builder().id("id_1").title("Schwedenreise")
                        .members(List.of(
                                new EventMember("Janice", 0),
                                new EventMember("Manu", 0),
                                new EventMember("Henny", 0)))
                        .expenditures(List.of()).build(),
                Event.builder().id("id_2").title("Kanutour")
                        .members(List.of(
                                new EventMember("Janice", 0),
                                new EventMember("Henny", 0)))
                        .expenditures(List.of()).build()
        ));

        userDb.deleteAll();
        String password = new BCryptPasswordEncoder().encode("superPassword123");
        userDb.save(TravelFoundUser.builder().username("henny").password(password).build());;
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
                .members(List.of(new EventMember("Julius", 0),
                        new EventMember("Henny", 0)))
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
    void testGetMapping() {
        // Given
        List<Event> eventList = List.of(
                Event.builder().id("id_1").title("Schwedenreise")
                        .members(List.of(
                                new EventMember("Janice", 0),
                                new EventMember("Manu", 0),
                                new EventMember("Henny", 0)))
                        .expenditures(List.of()).build(),
                Event.builder().id("id_2").title("Kanutour")
                        .members(List.of(
                                new EventMember("Janice", 0),
                                new EventMember("Henny", 0)))
                        .expenditures(List.of()).build()
        );

        // When
        HttpEntity<Void> entity = getValidAuthorizationEntity(null);
        ResponseEntity<Event[]> response = restTemplate.exchange(getEventUrl(), HttpMethod.GET, entity, Event[].class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(eventList.toArray()));
    }

    @Test
    void testPostMappingAddNewExpenditure(){
        // Given
        String eventId = "id_2";
        String expenditureId = "expenditure_id";
        Instant givenTime = Instant.parse("2020-11-22T18:00:00Z");

        AddExpenditureDto expenditureToBeAdded = AddExpenditureDto.builder()
                .description("Bahnfahrkarten")
                .members(List.of(new EventMember("Janice", 0),
                        new EventMember("Henny", 0)))
                .payer(new UserDto("Henny"))
                .amount(20)
                .build();

        Expenditure newExpenditure = Expenditure.builder()
                .id(expenditureId)
                .description("Bahnfahrkarten")
                .expenditurePerMemberList(List.of(
                        ExpenditurePerMember.builder().username("Henny").amount(10).build(),
                        ExpenditurePerMember.builder().username("Janice").amount(10).build()))
                .payer(new UserDto("Henny"))
                .amount(20)
                .timestamp(givenTime)
                .build();

        Event eventExpected = Event.builder()
                .id(eventId)
                .title("Kanutour")
                .members(List.of(new EventMember("Janice", -10),
                        new EventMember("Henny", 10)))
                .expenditures(new ArrayList<Expenditure>() {{
                    add(newExpenditure);
                }})
                .build();

        when(mockedIdUtils.generateId()).thenReturn(expenditureId);
        when(mockedtimestampUtils.generateTimestampEpochSeconds()).thenReturn(givenTime);

        // When
        HttpEntity<AddExpenditureDto> entity = getValidAuthorizationEntity(expenditureToBeAdded);
        ResponseEntity<Event> response = restTemplate.exchange(getEventUrl() + "/" +  eventId, HttpMethod.POST, entity, Event.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(eventExpected));
    }

    @Test
    void testPutMappingDeleteExpenditure(){
        // Given
        String eventId = "id_3";
        String expenditureId = "expenditure_id";
        IdsDto ids = IdsDto.builder().eventId(eventId).expenditureId(expenditureId).build();

        Expenditure expenditureToDelete = Expenditure.builder()
                .id(expenditureId)
                .description("Bahnfahrkarten")
                .expenditurePerMemberList(List.of(
                        ExpenditurePerMember.builder().username("Henny").amount(10).build(),
                        ExpenditurePerMember.builder().username("Janice").amount(10).build()))
                .payer(new UserDto("Henny"))
                .amount(20)
                .build();

        Event event = Event.builder()
                .id(eventId)
                .title("Kanutour")
                .members(List.of(new EventMember("Janice", 50),
                        new EventMember("Henny", 50)))
                .expenditures(List.of(expenditureToDelete))
                .build();

        eventDb.save(event);

        Event eventExpected = Event.builder()
                .id(eventId)
                .title("Kanutour")
                .members(List.of(new EventMember("Janice", 60),
                        new EventMember("Henny", 40)))
                .expenditures(List.of())
                .build();


        // When
        HttpEntity<IdsDto> entity = getValidAuthorizationEntity(ids);
        ResponseEntity<Event> response = restTemplate.exchange(getEventUrl() + "/expenditure/delete", HttpMethod.PUT, entity, Event.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(eventExpected));
    }
}
