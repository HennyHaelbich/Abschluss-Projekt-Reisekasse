package de.neuefische.henny.reisekasse.Service;

import de.neuefische.henny.reisekasse.Db.EventDb;
import de.neuefische.henny.reisekasse.Model.Dto.AddEventDto;
import de.neuefische.henny.reisekasse.Model.Event;
import de.neuefische.henny.reisekasse.Model.EventMember;
import de.neuefische.henny.reisekasse.utils.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final EventDb eventDb;
    private final IdUtils idUtils;

    @Autowired
    public EventService(EventDb eventDb, IdUtils idUtils) {
        this.eventDb = eventDb;
        this.idUtils = idUtils;
    }

    public Event addEvent(AddEventDto addEventDto){

        List<EventMember> eventMembers = addEventDto.getMembers().stream()
                .map(member -> new EventMember(member.getUsername(), 0.0))
                .collect(Collectors.toList());

        Event newEvent = Event.builder()
                .id(idUtils.generateId())
                .title(addEventDto.getTitle())
                .members(eventMembers)
                .expenditures(new ArrayList<>())
                .build();

        return eventDb.save(newEvent);
    }

    public List<Event> listEvents(){
        return eventDb.findAll();
    }
}
