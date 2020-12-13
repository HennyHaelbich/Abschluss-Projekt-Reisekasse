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
        userDb.deleteAll();

        String password = new BCryptPasswordEncoder().encode("superPassword123");
        userDb.save(TravelFoundUser.builder().username("henny").password(password).build());
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
                .members(List.of(new UserDto("Julius@web.de", "Julius", "Schmidt"),
                        new UserDto("Henny@web.de", "Henny", "Haelbich")))
                .build();
        when(mockedIdUtils.generateId()).thenReturn("id");

        Event expectedEvent = Event.builder()
                .id("id")
                .title("Norwegen 2020")
                .members(List.of(new EventMember("Henny@web.de", "Henny", "Haelbich", 0),
                        new EventMember("Julius@web.de", "Julius", "Schmidt", 0)))
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
                                new EventMember("janice", 0),
                                new EventMember("manu", 0),
                                new EventMember("henny", 0)))
                        .expenditures(List.of()).build(),
                Event.builder().id("id_2").title("Kanutour")
                        .members(List.of(
                                new EventMember("janice", 0),
                                new EventMember("torben", 0)))
                        .expenditures(List.of()).build()
        );
        eventDb.saveAll(eventList);

        List<Event> expectedEventList = List.of(
                Event.builder().id("id_1").title("Schwedenreise")
                        .members(List.of(
                                new EventMember("janice", 0),
                                new EventMember("manu", 0),
                                new EventMember("henny", 0)))
                        .expenditures(List.of()).build()
        );

        // When
        HttpEntity<Void> entity = getValidAuthorizationEntity(null);
        ResponseEntity<Event[]> response = restTemplate.exchange(getEventUrl(), HttpMethod.GET, entity, Event[].class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(expectedEventList.toArray()));
    }

    @Test
    void testPostMappingAddNewExpenditure(){
        // Given
        String eventId = "id_2";
        String expenditureId = "expenditure_id";

        userDb.save(TravelFoundUser.builder().username("Henny@web.de").firstName("Henny").lastName("Haelbich").build());

        eventDb.saveAll(List.of(
                Event.builder().id("id_2").title("Kanutour")
                        .members(List.of(
                                new EventMember("Henny@web.de", "Henny", "Haelbich", 0),
                                new EventMember("Janice@web.de", "Janice", "Schmidt", 0)))
                        .expenditures(List.of()).build()
        ));

        AddExpenditureDto expenditureToBeAdded = AddExpenditureDto.builder()
                .description("Bahnfahrkarten")
                .members(List.of(new EventMember("Henny@web.de", "Henny", "Haelbich", 0),
                        new EventMember("Janice@web.de", "Janice", "Schmidt", 0)))
                .payerId("Henny@web.de")
                .amount(20)
                .date("2020-11-22")
                .build();

        Expenditure newExpenditure = Expenditure.builder()
                .id(expenditureId)
                .description("Bahnfahrkarten")
                .expenditurePerMemberList(List.of(
                        ExpenditurePerMember.builder().username("Henny@web.de").firstName("Henny").lastName("Haelbich").amount(10).build(),
                        ExpenditurePerMember.builder().username("Janice@web.de").firstName("Janice").lastName("Schmidt").amount(10).build()))
                .payer(new UserDto("Henny@web.de", "Henny", "Haelbich"))
                .amount(20)
                .date("2020-11-22")
                .build();

        Event eventExpected = Event.builder()
                .id(eventId)
                .title("Kanutour")
                .members(List.of(new EventMember("Henny@web.de", "Henny", "Haelbich", 10),
                        new EventMember("Janice@web.de", "Janice", "Schmidt", -10)))
                .expenditures(new ArrayList<>() {{
                    add(newExpenditure);
                }})
                .build();

        when(mockedIdUtils.generateId()).thenReturn(expenditureId);

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

        Expenditure expenditureOne = Expenditure.builder()
                .id("id_1")
                .description("Bahnfahrkarten")
                .expenditurePerMemberList(List.of(
                        ExpenditurePerMember.builder().username("Henny").amount(10).build(),
                        ExpenditurePerMember.builder().username("Janice").amount(10).build()))
                .amount(20)
                .payer(UserDto.builder().username("Henny").build())
                .build();

        Expenditure expenditureToDelete = Expenditure.builder()
                .id(expenditureId)
                .description("Bahnfahrkarten")
                .expenditurePerMemberList(List.of(
                        ExpenditurePerMember.builder().username("Henny").amount(25).build(),
                        ExpenditurePerMember.builder().username("Janice").amount(25).build()))
                .amount(50)
                .payer(UserDto.builder().username("Janice").build())
                .build();

        Event eventBefore = Event.builder()
                .id(eventId)
                .title("Radreise")
                .members(List.of(
                        EventMember.builder().username("Henny").balance(15).build(),
                        EventMember.builder().username("Janice").balance(-15).build()))
                .expenditures(List.of(expenditureOne, expenditureToDelete))
                .build();

        eventDb.save(eventBefore);

        Event eventExpected = Event.builder()
                .id(eventId)
                .title("Radreise")
                .members(List.of(
                        EventMember.builder().username("Henny").balance(10).build(),
                        EventMember.builder().username("Janice").balance(-10).build()))
                .expenditures(List.of(expenditureOne))
                .build();


        // When
        HttpEntity<IdsDto> entity = getValidAuthorizationEntity(ids);
        ResponseEntity<Event> response = restTemplate.exchange(getEventUrl() + "/expenditure/delete", HttpMethod.PUT, entity, Event.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(eventExpected));
    }

    @Test
    void testPutMappingCompensateBalances(){
        // Given
        EventMembersDto givenEventMemberList = new EventMembersDto(List.of(
                EventMember.builder().username("Henny").balance(-20).build(),
                EventMember.builder().username("Janice").balance(-15).build(),
                EventMember.builder().username("Steffen").balance(-25).build(),
                EventMember.builder().username("Manu").balance(40).build(),
                EventMember.builder().username("Rene").balance(20).build()));

        List<Transfer> expectedTransfers = List.of(
                Transfer.builder()
                        .payer(new UserDto("Henny"))
                        .paymentReceiver(new UserDto("Rene"))
                        .amount(20)
                        .build(),
                Transfer.builder()
                        .payer(new UserDto("Janice"))
                        .paymentReceiver(new UserDto("Manu"))
                        .amount(15)
                        .build(),
                Transfer.builder()
                        .payer(new UserDto("Steffen"))
                        .paymentReceiver(new UserDto("Manu"))
                        .amount(25)
                        .build());

        // When
        HttpEntity<EventMembersDto> entity = getValidAuthorizationEntity(givenEventMemberList);
        ResponseEntity<Transfer[]> response = restTemplate.exchange(getEventUrl() + "/compensations", HttpMethod.PUT, entity, Transfer[].class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(expectedTransfers.toArray()));
    }
}
