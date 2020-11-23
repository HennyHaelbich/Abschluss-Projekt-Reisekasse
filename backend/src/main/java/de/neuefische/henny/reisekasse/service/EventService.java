package de.neuefische.henny.reisekasse.service;

import de.neuefische.henny.reisekasse.db.EventDb;
import de.neuefische.henny.reisekasse.model.Event;
import de.neuefische.henny.reisekasse.model.EventMember;
import de.neuefische.henny.reisekasse.model.Expenditure;
import de.neuefische.henny.reisekasse.model.dto.AddEventDto;
import de.neuefische.henny.reisekasse.model.dto.AddExpenditureDto;
import de.neuefische.henny.reisekasse.model.dto.UserDto;
import de.neuefische.henny.reisekasse.utils.IdUtils;
import de.neuefische.henny.reisekasse.utils.TimestampUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final EventDb eventDb;
    private final IdUtils idUtils;
    private final TimestampUtils timestampUtils;

    @Autowired
    public EventService(EventDb eventDb, IdUtils idUtils, TimestampUtils timestampUtils) {
        this.eventDb = eventDb;
        this.idUtils = idUtils;
        this.timestampUtils = timestampUtils;
    }

    public Event addEvent(AddEventDto addEventDto) {

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

    public List<Event> listEvents() {
        return eventDb.findAll();
    }

    public Event addExpenditure(AddExpenditureDto addExpenditureDto) {

        List<UserDto> memberDtos = addExpenditureDto.getMembers().stream()
                .map(member -> new UserDto(member.getUsername()))
                .collect(Collectors.toList());

        Expenditure newExpenditure = Expenditure.builder()
                .id(idUtils.generateId())
                .timestamp(timestampUtils.generateTimestampEpochSeconds())
                .members(memberDtos)
                .payer(addExpenditureDto.getPayer())
                .amount(addExpenditureDto.getAmount())
                .build();

        Event event = getEventById(addExpenditureDto.getEventId());
        event.getExpenditures().add(newExpenditure);

        List<EventMember> updatedEventMembers = setNewSaldo(addExpenditureDto.getMembers(), addExpenditureDto.getPayer(), addExpenditureDto.getAmount());
        event.setMembers(updatedEventMembers);

        return eventDb.save(event);

    }

    public Event getEventById(String eventId) {
        return eventDb.findById(eventId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<EventMember> setNewSaldo(List<EventMember> eventMembers, UserDto payer, double amount) {
        double amountPerPerson = amount / eventMembers.size();

        for (EventMember eventMember : eventMembers) {
            eventMember.setBalance(eventMember.getBalance() - amountPerPerson);

            if (eventMember.getUsername().equals(payer.getUsername())) {
                eventMember.setBalance(eventMember.getBalance() + amount);
            }
        }
        return eventMembers;
    }
}
