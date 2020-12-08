package de.neuefische.henny.reisekasse.service;

import de.neuefische.henny.reisekasse.db.EventDb;
import de.neuefische.henny.reisekasse.model.*;
import de.neuefische.henny.reisekasse.model.dto.AddExpenditureDto;
import de.neuefische.henny.reisekasse.model.dto.UserDto;
import de.neuefische.henny.reisekasse.utils.IdUtils;
import de.neuefische.henny.reisekasse.utils.TimestampUtils;
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
    void listEventsShouldGiveBackAllEventsInEventDb() {
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
    void testSetNewBalanceShouldGiveBackMemberListWithUpdatedBalance() {
        // Given
        List<EventMember> givenMemberList = List.of(
                EventMember.builder().username("Janice").balance(200).build(),
                EventMember.builder().username("Manu").balance(0).build(),
                EventMember.builder().username("Henny").balance(-500).build());

        List<ExpenditurePerMember> givenExpenditurePerMemberList = List.of(
                ExpenditurePerMember.builder().username("Manu").amount(333).build(),
                ExpenditurePerMember.builder().username("Janice").amount(333).build(),
                ExpenditurePerMember.builder().username("Henny").amount(334).build());

        UserDto payer = UserDto.builder().username("Manu").build();

        int amount = 1000;

        List<EventMember> expectedMemberList = List.of(
                EventMember.builder().username("Janice").balance(-133).build(),
                EventMember.builder().username("Manu").balance(667).build(),
                EventMember.builder().username("Henny").balance(-834).build());

        // When
         List<EventMember> result = eventService.updateBalance(givenMemberList, givenExpenditurePerMemberList, payer, amount, true);

        // Then
        assertThat(result, is(expectedMemberList));
    }

    @Test
    void testCalculateExpenditurePerMemberShouldReturnListWithExpenditurePerPerson() {
        // Given
        List<EventMember> givenMemberList = List.of(
                EventMember.builder().username("Janice").build(),
                EventMember.builder().username("Henny").build());
        int amount = 200;

        List<ExpenditurePerMember> expectedExpendituresList = List.of(
                ExpenditurePerMember.builder().username("Henny").amount(100).build(),
                ExpenditurePerMember.builder().username("Janice").amount(100).build());

        // When
        List<ExpenditurePerMember> result = eventService.calculateExpenditurePerMember(givenMemberList, amount);

        // Then
        assertThat(result, is(expectedExpendituresList));
    }

    @Test
    void testCalculateExpenditurePerMemberShouldReturnListWhereRestIsGivenToSomePerson() {
        // Given
        List<EventMember> givenMemberList = List.of(
                EventMember.builder().username("Janice").build(),
                EventMember.builder().username("Henny").build());
        int amount = 201;

        List<ExpenditurePerMember> expectedPossibilityOne = List.of(
                ExpenditurePerMember.builder().username("Henny").amount(100).build(),
                ExpenditurePerMember.builder().username("Janice").amount(101).build());

        List<ExpenditurePerMember> expectedPossibilityTwo = List.of(
                ExpenditurePerMember.builder().username("Henny").amount(101).build(),
                ExpenditurePerMember.builder().username("Janice").amount(100).build());

        // When
        List<ExpenditurePerMember> result = eventService.calculateExpenditurePerMember(givenMemberList, amount);

        // Then
        assertThat(result, anyOf(is(expectedPossibilityOne), is(expectedPossibilityTwo)));
    }

    @Test
    void testAddExpenditureShouldReturnEventWithAddedExpenditure() {
        // Given
        String eventId = "event_id";
        String expenditureId = "expenditure_id";
        Instant expectedTime = Instant.parse("2020-11-22T18:00:00Z");

        Event eventBefore = Event.builder()
                .id(eventId)
                .title("Radreise")
                .members(List.of(
                        EventMember.builder().username("Janice").balance(0).build(),
                        EventMember.builder().username("Henny").balance(0).build()))
                .expenditures(new ArrayList<>())
                .build();

        AddExpenditureDto expenditureToBeAdded = AddExpenditureDto.builder()
                .description("Bahnfahrkarten")
                .members(List.of(
                        EventMember.builder().username("Janice").balance(0).build(),
                        EventMember.builder().username("Henny").balance(0).build()))
                .payerId("Henny")
                .amount(20)
                .build();

        Expenditure newExpenditure = Expenditure.builder()
                .id(expenditureId)
                .description("Bahnfahrkarten")
                .expenditurePerMemberList(List.of(
                        ExpenditurePerMember.builder().username("Henny").amount(10).build(),
                        ExpenditurePerMember.builder().username("Janice").amount(10).build()))
                .amount(20)
                .timestamp(expectedTime)
                .payer(UserDto.builder().username("Henny").build())
                .build();

        Event eventExpected = Event.builder()
                .id(eventId)
                .title("Radreise")
                .members(List.of(
                        EventMember.builder().username("Janice").balance(-10).build(),
                        EventMember.builder().username("Henny").balance(10).build()))
                .expenditures(new ArrayList<>() {{
                    add(newExpenditure);
                }})
                .build();

        when(mockIdUtils.generateId()).thenReturn(expenditureId);
        when(mockTimestampUtils.generateTimestampEpochSeconds()).thenReturn(expectedTime);
        when(mockEventDb.findById(eventId)).thenReturn(Optional.of(eventBefore));
        when(mockEventDb.save(eventExpected)).thenReturn(eventExpected);
        when(mockUserService.getUserById("Henny")).thenReturn(Optional.of(UserDto.builder().username("Henny").build()));

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
    void testCompensateBalanceShouldFindeOneToOneMatchesAndReturnListOfTransfers() {
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
}