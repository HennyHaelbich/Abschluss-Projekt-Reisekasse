package de.neuefische.henny.reisekasse.controller;

import de.neuefische.henny.reisekasse.db.EventDb;
import de.neuefische.henny.reisekasse.model.Event;
import de.neuefische.henny.reisekasse.model.EventMember;
import de.neuefische.henny.reisekasse.model.Expenditure;
import de.neuefische.henny.reisekasse.model.dto.AddEventDto;
import de.neuefische.henny.reisekasse.model.dto.AddExpenditureDto;
import de.neuefische.henny.reisekasse.model.dto.UserDto;
import de.neuefische.henny.reisekasse.utils.IdUtils;
import de.neuefische.henny.reisekasse.utils.TimestampUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

    @BeforeEach
    public void setupDb() {
        eventDb.deleteAll();
        eventDb.saveAll(List.of(
                Event.builder().id("id_1").title("Schwedenreise")
                        .members(List.of(
                                new EventMember("Janice", 0.0),
                                new EventMember("Manu", 0.0),
                                new EventMember("Henny", 0.0)))
                        .expenditures(List.of()).build(),
                Event.builder().id("id_2").title("Norwegen 2020")
                        .members(List.of(
                                new EventMember("Julius", 0.0),
                                new EventMember("Henny", 0.0),
                                new EventMember("Manu", 0.0)))
                        .expenditures(List.of()).build()
        ));
    }

    private String getEventUrl() {
        return "http://localhost:" + port + "/api/events";
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
        ResponseEntity<Event> response = restTemplate.postForEntity(getEventUrl(), eventToBeAdded, Event.class);

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
                                new EventMember("Janice", 0.0),
                                new EventMember("Manu", 0.0),
                                new EventMember("Henny", 0.0)))
                        .expenditures(List.of()).build(),
                Event.builder().id("id_2").title("Norwegen 2020")
                        .members(List.of(
                                new EventMember("Julius", 0.0),
                                new EventMember("Henny", 0.0),
                                new EventMember("Manu", 0.0)))
                        .expenditures(List.of()).build());

        // When
        ResponseEntity<Event[]> response = restTemplate.getForEntity(getEventUrl(), Event[].class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(eventList.toArray()));
    }

    @Test
    void testSetNewSaldo(){
        // Given
        String eventId = "id_1";
        String expenditureId = "expenditure_id";
        Instant expectedTime = Instant.parse("2020-11-22T18:00:00Z");

        AddExpenditureDto expenditureToBeAdded = AddExpenditureDto.builder()
                .eventId(eventId)
                .members(List.of(new EventMember("Janice", 0.0),
                        new EventMember("Manu", 0.0),
                        new EventMember("Henny", 0.0)))
                .payer(new UserDto("Manu"))
                .amount(15)
                .build();

        Expenditure newExpenditure = Expenditure.builder()
                .id(expenditureId)
                .members(List.of(new UserDto("Janice"), new UserDto("Manu"), new UserDto("Henny")))
                .payer(new UserDto("Manu"))
                .amount(15.0)
                .timestamp(expectedTime)
                .build();

        Event eventExpected = Event.builder()
                .id(eventId)
                .title("Schwedenreise")
                .members(List.of(new EventMember("Janice", -5.0),
                        new EventMember("Manu", 10.0),
                        new EventMember("Henny", -5.0)))
                .expenditures(new ArrayList<Expenditure>() {{
                    add(newExpenditure);
                }})
                .build();

        when(mockedIdUtils.generateId()).thenReturn(expenditureId);
        when(mockedtimestampUtils.generateTimestampEpochSeconds()).thenReturn(expectedTime);

        // When
        ResponseEntity<Event> response = restTemplate.postForEntity(getEventUrl() + "/" +  eventId, expenditureToBeAdded, Event.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(eventExpected));
    }
}
