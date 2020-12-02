package de.neuefische.henny.reisekasse.controller;

import de.neuefische.henny.reisekasse.model.Event;
import de.neuefische.henny.reisekasse.model.dto.AddEventDto;
import de.neuefische.henny.reisekasse.model.dto.AddExpenditureDto;
import de.neuefische.henny.reisekasse.model.dto.IdsDto;
import de.neuefische.henny.reisekasse.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public Event addEvent(@RequestBody AddEventDto addEventDto) {
        return eventService.addEvent(addEventDto);
    }

    @GetMapping
    public List<Event> getEvents(Principal principal) {
        return eventService.listEvents(principal.getName());
    }

    @PostMapping("{eventId}")
    public Event addExpenditure(@PathVariable String eventId, @RequestBody AddExpenditureDto addExpenditureDto) {
        return eventService.addExpenditure(eventId, addExpenditureDto);
    }

   @PutMapping("/expenditure/delete")
    public Event deleteExpenditure(@RequestBody IdsDto ids) {
        return eventService.deleteExpenditure(ids.getEventId(), ids.getExpenditureId());
    }

}
