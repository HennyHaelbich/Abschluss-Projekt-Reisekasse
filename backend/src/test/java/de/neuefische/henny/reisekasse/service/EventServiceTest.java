package de.neuefische.henny.reisekasse.service;

import de.neuefische.henny.reisekasse.db.EventDb;
import de.neuefische.henny.reisekasse.model.*;
import de.neuefische.henny.reisekasse.model.dto.AddExpenditureDto;
import de.neuefische.henny.reisekasse.model.dto.UserDto;
import de.neuefische.henny.reisekasse.utils.IdUtils;
import de.neuefische.henny.reisekasse.utils.TimestampUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.anyOf;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EventServiceTest {
    private final EventDb mockEventDb = mock(EventDb.class);
    private final IdUtils mockIdUtils = mock(IdUtils.class);
    private final TimestampUtils mockTimestampUtils = mock(TimestampUtils.class);
    private final MongoTemplate mockMongoTemplate = mock(MongoTemplate.class);
    private final UserService mockUserService = mock(UserService.class);
    private final EventService eventService = new EventService(mockEventDb, mockIdUtils, mockTimestampUtils, mockMongoTemplate, mockUserService);

    @Test
    void listEventsShouldGiveBackAllEventsIncludingTheUserInEventDb() {
        // Given
        String username = "Janice";

        List<Event> eventListOfUser = List.of(
                Event.builder().id("id_1").title("Schwedenreise")
                        .members(List.of(EventMember.builder().username("Janice").balance(0).build(),
                                EventMember.builder().username("Manu").balance(0).build()))
                        .expenditures(new ArrayList<>()).build());

        Query query = new Query();
        query.addCriteria(Criteria.where("members.username").is(username));
        when(mockMongoTemplate.find(query, Event.class)).thenReturn(eventListOfUser);

        // When
        List<Event> userEvents = eventService.listEvents(username);

        // Then
        assertThat(userEvents, is(eventListOfUser));
    }

    @Test
    void testGetEventByIdShouldReturnTheSpecifiedEvent() {
        // Given
        String eventId = "uniqueId";
        Event expectedEvent = Event.builder().id("id_1").title("Schwedenreise")
                .members(List.of(EventMember.builder().username("Janice").balance(0).build(),
                        EventMember.builder().username("Manu").balance(0).build()))
                .expenditures(new ArrayList<>()).build();
        when(mockEventDb.findById(eventId)).thenReturn(Optional.of(expectedEvent));

        // When
        Event result = eventService.getEventById(eventId);

        // Then
        assertThat(result, is(expectedEvent));
    }

    @Test
    void testGetEventByIdShouldThrowExceptionWhenIdNotFound() {
        // Given
        String eventId = "uniqueId";
        when(mockEventDb.findById(eventId)).thenReturn(Optional.empty());

        // When - Then
        try {
            eventService.getEventById(eventId);
            fail();
        } catch (ResponseStatusException exception) {
            assertThat(exception.getStatus(), is(HttpStatus.NOT_FOUND));
        }
    }

    @Test
    @DisplayName("calculateExpenditurePerMember should return list with expenditure per person in alphabetical order of " +
            "member first and last name")
    void testCalculateExpenditurePerMemberWithDivisibleAmount() {
        // Given
        List<EventMember> givenMemberList = List.of(
                EventMember.builder().username("Janice@web.de").firstName("Janice").lastName("Mayer").build(),
                EventMember.builder().username("henny@web.de").firstName("Henny").lastName("Haelbich").build());
        int amount = 200;

        List<ExpenditurePerMember> expectedExpendituresList = List.of(
                ExpenditurePerMember.builder().username("henny@web.de").firstName("Henny").lastName("Haelbich").amount(100).build(),
                ExpenditurePerMember.builder().username("Janice@web.de").firstName("Janice").lastName("Mayer").amount(100).build());

        // When
        List<ExpenditurePerMember> result = eventService.calculateExpenditurePerMember(givenMemberList, amount);

        // Then
        assertThat(result, is(expectedExpendituresList));
    }

    @Test
    @DisplayName("calculateExpenditurePerMember should return list with expenditure per person in alphabetical order of " +
            "member first and last name where remaining cents where randomly distributed (not more than one cent per person)")
    void testCalculateExpenditurePerMemberWithIndivisibleAmount() {
        // Given
        List<EventMember> givenMemberList = List.of(
                EventMember.builder().username("Janice@web.de").firstName("Janice").lastName("Mayer").build(),
                EventMember.builder().username("henny@web.de").firstName("Henny").lastName("Haelbich").build());
        int amount = 201;

        List<ExpenditurePerMember> expectedPossibilityOne = List.of(
                ExpenditurePerMember.builder().username("henny@web.de").firstName("Henny").lastName("Haelbich").amount(100).build(),
                ExpenditurePerMember.builder().username("Janice@web.de").firstName("Janice").lastName("Mayer").amount(101).build());

        List<ExpenditurePerMember> expectedPossibilityTwo = List.of(
                ExpenditurePerMember.builder().username("henny@web.de").firstName("Henny").lastName("Haelbich").amount(101).build(),
                ExpenditurePerMember.builder().username("Janice@web.de").firstName("Janice").lastName("Mayer").amount(100).build());

        // When
        List<ExpenditurePerMember> result = eventService.calculateExpenditurePerMember(givenMemberList, amount);

        // Then
        assertThat(result, anyOf(is(expectedPossibilityOne), is(expectedPossibilityTwo)));
    }

    @Test
    @DisplayName("AddExpenditure should return event with added expenditure the newly added expenditure stands on the first place")
    void testAddExpenditure() {
        // Given
        String eventId = "event_id";
        String expenditureId = "expenditure_id";
        Instant expectedTime = Instant.parse("2020-11-22T18:00:00Z");

        Expenditure expenditureOne = Expenditure.builder()
                .id(expenditureId)
                .description("Kaffee und Kuchen")
                .expenditurePerMemberList(List.of(
                        ExpenditurePerMember.builder().username("henny@web.de").firstName("Henny").lastName("Haelbich").amount(25).build(),
                        ExpenditurePerMember.builder().username("Janice@web.de").firstName("Janice").lastName("Mayer").amount(25).build()))
                .amount(50)
                .timestamp(Instant.parse("2020-11-22T15:00:00Z"))
                .payer(UserDto.builder().username("henny@web.de").firstName("Henny").lastName("Haelbich").build())
                .build();

        Event eventBefore = Event.builder()
                .id(eventId)
                .title("Radreise")
                .members(List.of(
                        EventMember.builder().username("henny@web.de").firstName("Henny").lastName("Haelbich").balance(25).build(),
                        EventMember.builder().username("Janice@web.de").firstName("Janice").lastName("Mayer").balance(-25).build()))
                .expenditures(new ArrayList<>() {{ add(expenditureOne); }})
                .build();

        AddExpenditureDto expenditureToBeAdded = AddExpenditureDto.builder()
                .description("Bahnfahrkarten")
                .members(List.of(
                        EventMember.builder().username("henny@web.de").firstName("Henny").lastName("Haelbich").balance(25).build(),
                        EventMember.builder().username("Janice@web.de").firstName("Janice").lastName("Mayer").balance(-25).build()))
                .payerId("Henny")
                .amount(20)
                .build();

        Expenditure newExpenditure = Expenditure.builder()
                .id(expenditureId)
                .description("Bahnfahrkarten")
                .expenditurePerMemberList(List.of(
                        ExpenditurePerMember.builder().username("henny@web.de").firstName("Henny").lastName("Haelbich").amount(10).build(),
                        ExpenditurePerMember.builder().username("Janice@web.de").firstName("Janice").lastName("Mayer").amount(10).build()))
                .amount(20)
                .timestamp(expectedTime)
                .payer(UserDto.builder().username("henny@web.de").firstName("Henny").lastName("Haelbich").build())
                .build();

        Event eventExpected = Event.builder()
                .id(eventId)
                .title("Radreise")
                .members(List.of(
                        EventMember.builder().username("henny@web.de").firstName("Henny").lastName("Haelbich").balance(35).build(),
                        EventMember.builder().username("Janice@web.de").firstName("Janice").lastName("Mayer").balance(-35).build()))
                .expenditures(new ArrayList<>() {{ add(newExpenditure); add(expenditureOne); }})
                .build();

        when(mockIdUtils.generateId()).thenReturn(expenditureId);
        when(mockTimestampUtils.generateTimestampEpochSeconds()).thenReturn(expectedTime);
        when(mockEventDb.findById(eventId)).thenReturn(Optional.of(eventBefore));
        when(mockEventDb.save(eventExpected)).thenReturn(eventExpected);
        when(mockUserService.getUserById("Henny")).thenReturn(Optional.of(UserDto.builder().username("henny@web.de").firstName("Henny").lastName("Haelbich").build()));

        // When
        Event result = eventService.addExpenditure(eventId, expenditureToBeAdded);

        // Then
        assertThat(result, is(eventExpected));
    }

    @Test
    void testDeleteExpenditure() {
        // Given
        String eventId = "event_id";
        String idToDelete = "id_2";
        Instant expectedTime = Instant.parse("2020-11-22T18:00:00Z");

        Expenditure expenditureOne = Expenditure.builder()
                .id("id_1")
                .description("Bahnfahrkarten")
                .expenditurePerMemberList(List.of(
                        ExpenditurePerMember.builder().username("Henny").amount(10).build(),
                        ExpenditurePerMember.builder().username("Janice").amount(10).build()))
                .amount(20)
                .timestamp(expectedTime)
                .payer(UserDto.builder().username("Henny").build())
                .build();

        Expenditure expenditureToDelete = Expenditure.builder()
                .id(idToDelete)
                .description("Bahnfahrkarten")
                .expenditurePerMemberList(List.of(
                        ExpenditurePerMember.builder().username("Henny").amount(25).build(),
                        ExpenditurePerMember.builder().username("Janice").amount(25).build()))
                .amount(50)
                .timestamp(expectedTime)
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

        Event eventExpected = Event.builder()
                .id(eventId)
                .title("Radreise")
                .members(List.of(
                        EventMember.builder().username("Henny").balance(10).build(),
                        EventMember.builder().username("Janice").balance(-10).build()))
                .expenditures(List.of(expenditureOne))
                .build();

        when(mockEventDb.findById(eventId)).thenReturn(Optional.of(eventBefore));
        when(mockEventDb.save(eventExpected)).thenReturn(eventExpected);

        // When
        Event result = eventService.deleteExpenditure(eventId, idToDelete);

        // Then
        assertThat(result, is(eventExpected));
    }

    @Test
    void testCalculateBalance() {
        // Given

        Instant expectedTime = Instant.parse("2020-11-22T18:00:00Z");

        Expenditure expenditureOne = Expenditure.builder()
                .id("id_1")
                .description("Bahnfahrkarten")
                .expenditurePerMemberList(List.of(
                        ExpenditurePerMember.builder().username("Henny").amount(10).build(),
                        ExpenditurePerMember.builder().username("Janice").amount(10).build()))
                .amount(20)
                .timestamp(expectedTime)
                .payer(UserDto.builder().username("Henny").build())
                .build();

        Expenditure expenditureTwo = Expenditure.builder()
                .id("id_2")
                .description("Kaffee und Kuchen")
                .expenditurePerMemberList(List.of(
                        ExpenditurePerMember.builder().username("Henny").amount(25).build(),
                        ExpenditurePerMember.builder().username("Janice").amount(25).build()))
                .amount(50)
                .timestamp(expectedTime)
                .payer(UserDto.builder().username("Janice").build())
                .build();

        List<Expenditure> expenditureList = List.of(expenditureOne, expenditureTwo);

        List<EventMember> eventMemberList = List.of(
                        EventMember.builder().username("Henny").build(),
                        EventMember.builder().username("Janice").build());

        List<EventMember> expectedEventMemberList = List.of(
                EventMember.builder().username("Henny").balance(-15).build(),
                EventMember.builder().username("Janice").balance(15).build());

        // When
        List<EventMember> result = eventService.calculateBalance(eventMemberList, expenditureList);

        // Then
        assertThat(result, is(expectedEventMemberList));
    }

    @Test
    void testCompensateBalancesOfSubgroupShouldReturnListOfTransfers() {
        // Given
        List<EventMember> paymentReceivers = new ArrayList<>();
        paymentReceivers.add(EventMember.builder().username("Rene").balance(20).build());
        paymentReceivers.add(EventMember.builder().username("Manu").balance(43).build());

        List<EventMember> payers = new ArrayList<>();
        payers.add(EventMember.builder().username("Janice").balance(-15).build());
        payers.add(EventMember.builder().username("Henny").balance(-23).build());
        payers.add(EventMember.builder().username("Steffen").balance(-25).build());

        List<Transfer> expectedTransfers = List.of(
                Transfer.builder()
                        .payer(new UserDto("Janice"))
                        .paymentReceiver(new UserDto("Rene"))
                        .amount(15)
                        .build(),
                Transfer.builder()
                        .payer(new UserDto("Henny"))
                        .paymentReceiver(new UserDto("Rene"))
                        .amount(5)
                        .build(),
                Transfer.builder()
                        .payer(new UserDto("Henny"))
                        .paymentReceiver(new UserDto("Manu"))
                        .amount(18)
                        .build(),
                Transfer.builder()
                        .payer(new UserDto("Steffen"))
                        .paymentReceiver(new UserDto("Manu"))
                        .amount(25)
                        .build());

        // When
        List<Transfer> result = eventService.compensateBalancesOfSubgroup(paymentReceivers, payers);

        // Then
        assertThat(result, is(expectedTransfers));
    }

    @Test
    @DisplayName("compensateBalance should finde one to one matches and return list of transfers")
    void testCompensateBalanceWithOneToOneMatch() {
        // Given
        List<EventMember> givenEventMemberList = List.of(
                EventMember.builder().username("Henny").balance(-20).build(),
                EventMember.builder().username("Janice").balance(-15).build(),
                EventMember.builder().username("Steffen").balance(-25).build(),
                EventMember.builder().username("Manu").balance(40).build(),
                EventMember.builder().username("Rene").balance(20).build());

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
        List<Transfer> result = eventService.compensateBalances(givenEventMemberList);

        // Then
        assertThat(result, is(expectedTransfers));
    }

    @Test
    @DisplayName("compensateBalance should finde one to one matches and return list of transfers")
    void testCompensateBalanceWithTwoOneToOneMatches() {
        // Given
        List<EventMember> givenEventMemberList = List.of(
                EventMember.builder().username("Henny").balance(10).build(),
                EventMember.builder().username("Janice").balance(20).build(),
                EventMember.builder().username("Steffen").balance(20).build(),
                EventMember.builder().username("Manu").balance(-20).build(),
                EventMember.builder().username("Rene").balance(-30).build());

        List<Transfer> expectedTransfers = List.of(
                Transfer.builder()
                        .payer(new UserDto("Manu"))
                        .paymentReceiver(new UserDto("Janice"))
                        .amount(20)
                        .build(),
                Transfer.builder()
                        .payer(new UserDto("Rene"))
                        .paymentReceiver(new UserDto("Henny"))
                        .amount(10)
                        .build(),
                Transfer.builder()
                        .payer(new UserDto("Rene"))
                        .paymentReceiver(new UserDto("Steffen"))
                        .amount(20)
                        .build());

        // When
        List<Transfer> result = eventService.compensateBalances(givenEventMemberList);

        // Then
        assertThat(result, is(expectedTransfers));
    }

    @Test
    @DisplayName("compensateBalance for two personen should return transfer")
    void testCompensateBalanceWithTwoPersons() {
        // Given
        List<EventMember> givenEventMemberList = List.of(
                EventMember.builder().username("Henny").balance(-15).build(),
                EventMember.builder().username("Janice").balance(15).build());

        List<Transfer> expectedTransfers = List.of(
                Transfer.builder()
                        .payer(new UserDto("Henny"))
                        .paymentReceiver(new UserDto("Janice"))
                        .amount(15)
                        .build());

        // When
        List<Transfer> result = eventService.compensateBalances(givenEventMemberList);

        // Then
        assertThat(result, is(expectedTransfers));
    }
}