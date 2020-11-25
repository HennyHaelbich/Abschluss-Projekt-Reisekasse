package de.neuefische.henny.reisekasse.controller;

import de.neuefische.henny.reisekasse.model.dto.AddEventDto;
import de.neuefische.henny.reisekasse.model.Event;
import de.neuefische.henny.reisekasse.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Event addEvent(@RequestBody AddEventDto addEventDto){
        return eventService.addEvent(addEventDto);
    }

    @GetMapping
    public List<Event> getEvents(){
        return eventService.listEvents();
    }
}
