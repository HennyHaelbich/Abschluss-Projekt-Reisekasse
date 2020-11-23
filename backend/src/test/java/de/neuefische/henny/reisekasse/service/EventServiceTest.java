package de.neuefische.henny.reisekasse.service;

import de.neuefische.henny.reisekasse.db.EventDb;
import de.neuefische.henny.reisekasse.model.Event;
import de.neuefische.henny.reisekasse.model.EventMember;
import de.neuefische.henny.reisekasse.model.Expenditure;
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
                        .members(List.of(new EventMember("Janice", 0.0), new EventMember("Manu", 0)))
                        .expenditures(new ArrayList<>()).build(),
                Event.builder().id("id_2").title("Norwegen 2020")
                        .members(List.of(new EventMember("Julius", 0.0), new EventMember("Henny", 0)))
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
                .members(List.of(new EventMember("Janice", 0.0), new EventMember("Manu", 0)))
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

/*
    @Test
    void testSetNewBalanceShouldGiveBackMemberListWithUpdatedBalance() {
        // Given
        List<EventMember> givenMemberList = List.of(new EventMember("Janice", 2.0),
                new EventMember("Manu", 0.0),
                new EventMember("Henny", -5.0));
        UserDto payer = UserDto("Manu");
        double amount = 15;

        List<EventMember> expectedMemberList = List.of(new EventMember("Janice", -3.0),
                new EventMember("Manu", 10.0),
                new EventMember("Henny", -10.0));

        // When
        List<EventMember> result = eventService.setNewSaldo(givenMemberList, payerId, amount);

        // Then
        assertThat(result, is(expectedMemberList));
    }
*/

    @Test
    void testAddExpenditureShouldReturnEventWithAddedExpenditure() {
        // Given
        String eventId = "event_id";
        String expenditureId = "expenditure_id";
        Instant expectedTime = Instant.parse("2020-11-22T18:00:00Z");

        Event eventBefore = Event.builder()
                .id(eventId)
                .title("Radreise")
                .members(List.of(new EventMember("Janice", 0.0),
                        new EventMember("Manu", 0.0),
                        new EventMember("Henny", 0.0)))
                .expenditures(new ArrayList<>())
                .build();

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
                .title("Radreise")
                .members(List.of(new EventMember("Janice", -5.0),
                        new EventMember("Manu", 10.0),
                        new EventMember("Henny", -5.0)))
                .expenditures(new ArrayList<Expenditure>() {{
                    add(newExpenditure);
                }})
                .build();

        when(mockIdUtils.generateId()).thenReturn(expenditureId);
        when(mockTimestampUtils.generateTimestampEpochSeconds()).thenReturn(expectedTime);
        when(mockEventDb.findById(eventId)).thenReturn(Optional.of(eventBefore));
        when(mockEventDb.save(eventExpected)).thenReturn(eventExpected);

        // When
        Event result = eventService.addExpenditure(expenditureToBeAdded);

        // Then
        assertThat(result, is(eventExpected));
    }

}