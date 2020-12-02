package de.neuefische.henny.reisekasse.service;

import de.neuefische.henny.reisekasse.db.EventDb;
import de.neuefische.henny.reisekasse.model.Event;
import de.neuefische.henny.reisekasse.model.EventMember;
import de.neuefische.henny.reisekasse.model.Expenditure;
import de.neuefische.henny.reisekasse.model.ExpenditurePerMember;
import de.neuefische.henny.reisekasse.model.dto.AddExpenditureDto;
import de.neuefische.henny.reisekasse.model.dto.UserDto;
import de.neuefische.henny.reisekasse.utils.IdUtils;
import de.neuefische.henny.reisekasse.utils.TimestampUtils;
import org.junit.jupiter.api.Test;
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
    final EventDb mockEventDb = mock(EventDb.class);
    final IdUtils mockIdUtils = mock(IdUtils.class);
    final TimestampUtils mockTimestampUtils = mock(TimestampUtils.class);
    final EventService eventService = new EventService(mockEventDb, mockIdUtils, mockTimestampUtils);

    @Test
    void listEventsShouldGiveBackAllEventsInEventDb() {
        // Given
        List<Event> eventList = List.of(
                Event.builder().id("id_1").title("Schwedenreise")
                        .members(List.of(EventMember.builder().username("Janice").balance(0).build(),
                                EventMember.builder().username("Manu").balance(0).build()))
                        .expenditures(new ArrayList<>()).build(),
                Event.builder().id("id_2").title("Norwegen 2020")
                        .members(List.of(EventMember.builder().username("Julius").balance(0).build(),
                                EventMember.builder().username("Henny").balance(0).build()))
                        .expenditures(new ArrayList<>()).build()
        );
        when(mockEventDb.findAll()).thenReturn(eventList);

        // When
        List<Event> allEvents = eventService.listEvents();

        // Then
        assertThat(allEvents, is(eventList));
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
                .payer(UserDto.builder().username("Henny").build())
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

        // When
        Event result = eventService.addExpenditure(eventId, expenditureToBeAdded);

        // Then
        assertThat(result, is(eventExpected));
    }

    @Test
    void testDeleteExpenditureShouldReturnEventWithDeletedExpenditure() {
        // Given
        String eventId = "event_id";
        String expenditureId = "expenditure_id";
        Instant expectedTime = Instant.parse("2020-11-22T18:00:00Z");

        Expenditure expenditureToDelete = Expenditure.builder()
                .id(expenditureId)
                .description("Bahnfahrkarten")
                .expenditurePerMemberList(List.of(
                        ExpenditurePerMember.builder().username("Henny").amount(10).build(),
                        ExpenditurePerMember.builder().username("Janice").amount(10).build()))
                .amount(20)
                .timestamp(expectedTime)
                .payer(UserDto.builder().username("Henny").build())
                .build();

        Event eventBefore = Event.builder()
                .id(eventId)
                .title("Radreise")
                .members(List.of(
                        EventMember.builder().username("Janice").balance(50).build(),
                        EventMember.builder().username("Henny").balance(50).build()))
                .expenditures(List.of(expenditureToDelete))
                .build();

        Event eventExpected = Event.builder()
                .id(eventId)
                .title("Radreise")
                .members(List.of(
                        EventMember.builder().username("Janice").balance(60).build(),
                        EventMember.builder().username("Henny").balance(40).build()))
                .expenditures(List.of())
                .build();

        when(mockEventDb.findById(eventId)).thenReturn(Optional.of(eventBefore));
        when(mockEventDb.save(eventExpected)).thenReturn(eventExpected);

        // When
        Event result = eventService.deleteExpenditure(eventId, expenditureId);

        // Then
        assertThat(result, is(eventExpected));
    }
}