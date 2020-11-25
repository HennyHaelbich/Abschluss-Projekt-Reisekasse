package de.neuefische.henny.reisekasse.service;

import de.neuefische.henny.reisekasse.db.EventDb;
import de.neuefische.henny.reisekasse.model.Event;
import de.neuefische.henny.reisekasse.model.EventMember;
import de.neuefische.henny.reisekasse.utils.IdUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class EventServiceTest {
    final EventDb mockEventDb = mock(EventDb.class);
    final IdUtils mockIdUtils = mock(IdUtils.class);
    final EventService eventService = new EventService(mockEventDb, mockIdUtils);

    @Test
    void listEventsShouldGiveBackAllEventsInEventDb() {
        // Given
        List<Event> eventList = List.of(
                Event.builder().id("id_1").title("Schwedenreise")
                        .members(List.of(new EventMember("Janice", 0.0), new EventMember("Manu", 0)))
                .expenditures(List.of()).build(),
                Event.builder().id("id_2").title("Norwegen 2020")
                        .members(List.of(new EventMember("Julius", 0.0), new EventMember("Henny", 0)))
                        .expenditures(List.of()).build()
        );
        when(mockEventDb.findAll()).thenReturn(eventList);

        // When
        List<Event> allEvents = eventService.listEvents();

        // Then
        assertThat(allEvents, is(eventList));
    }
}
