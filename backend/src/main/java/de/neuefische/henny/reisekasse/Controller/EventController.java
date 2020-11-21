package de.neuefische.henny.reisekasse.Controller;

import de.neuefische.henny.reisekasse.Model.Dto.AddEventDto;
import de.neuefische.henny.reisekasse.Model.Event;
import de.neuefische.henny.reisekasse.Service.EventService;
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
