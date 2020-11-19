package de.neuefische.henny.reisekasse.Service;

import de.neuefische.henny.reisekasse.Db.EventDb;
import de.neuefische.henny.reisekasse.Model.Dto.AddEventDto;
import de.neuefische.henny.reisekasse.Model.Event;
import de.neuefische.henny.reisekasse.Model.Expenditures;
import de.neuefische.henny.reisekasse.utils.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
        Event newEvent = Event.builder()
                .id(idUtils.generateId())
                .title(addEventDto.getTitle())
                .members(addEventDto.getMembers())
                .expenditures(new ArrayList<Expenditures>())
                .build();

        return eventDb.save(newEvent);
    }
}
