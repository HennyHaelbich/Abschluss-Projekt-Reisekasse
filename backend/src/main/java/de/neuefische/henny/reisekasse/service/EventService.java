package de.neuefische.henny.reisekasse.service;

import de.neuefische.henny.reisekasse.db.EventDb;
import de.neuefische.henny.reisekasse.model.Event;
import de.neuefische.henny.reisekasse.model.EventMember;
import de.neuefische.henny.reisekasse.model.Expenditure;
import de.neuefische.henny.reisekasse.model.ExpenditurePerMember;
import de.neuefische.henny.reisekasse.model.dto.AddEventDto;
import de.neuefische.henny.reisekasse.model.dto.AddExpenditureDto;
import de.neuefische.henny.reisekasse.model.dto.UserDto;
import de.neuefische.henny.reisekasse.utils.IdUtils;
import de.neuefische.henny.reisekasse.utils.TimestampUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final EventDb eventDb;
    private final IdUtils idUtils;
    private final TimestampUtils timestampUtils;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public EventService(EventDb eventDb, IdUtils idUtils, TimestampUtils timestampUtils, MongoTemplate mongoTemplate) {
        this.eventDb = eventDb;
        this.idUtils = idUtils;
        this.timestampUtils = timestampUtils;
        this.mongoTemplate = mongoTemplate;
    }


    public Event addEvent(AddEventDto addEventDto) {

        List<EventMember> eventMembers = addEventDto.getMembers().stream()
                .map(member -> EventMember.builder()
                        .username(member.getUsername())
                        .firstName(member.getFirstName())
                        .lastName(member.getLastName())
                        .balance(0)
                        .build())
                .collect(Collectors.toList());

        Event newEvent = Event.builder()
                .id(idUtils.generateId())
                .title(addEventDto.getTitle())
                .members(eventMembers)
                .expenditures(new ArrayList<>())
                .build();

        return eventDb.save(newEvent);
    }

    public List<Event> listEvents(String username) {

        Query query = new Query();
        query.addCriteria(Criteria.where("members.username").is(username));

        return mongoTemplate.find(query, Event.class);
    }


    public Event addExpenditure(String eventId, AddExpenditureDto addExpenditureDto) {

        List<ExpenditurePerMember> expenditurePerMemberList =
                calculateExpenditurePerMember(addExpenditureDto.getMembers(), addExpenditureDto.getAmount());

        Expenditure newExpenditure = Expenditure.builder()
                .id(idUtils.generateId())
                .timestamp(timestampUtils.generateTimestampEpochSeconds())
                .description(addExpenditureDto.getDescription())
                .expenditurePerMemberList(expenditurePerMemberList)
                .payer(addExpenditureDto.getPayer())
                .amount(addExpenditureDto.getAmount())
                .build();

        // get Event from Database
        Event event = getEventById(eventId);

        event.getExpenditures().add(newExpenditure);

        List<EventMember> updatedEventMembers =
                setNewBalance(addExpenditureDto.getMembers(), expenditurePerMemberList, addExpenditureDto.getPayer(), addExpenditureDto.getAmount());

        event.setMembers(updatedEventMembers);

        return eventDb.save(event);

    }


    public Event getEventById(String eventId) {
        return eventDb.findById(eventId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<ExpenditurePerMember> calculateExpenditurePerMember(List<EventMember> eventMembers, int amount) {
        int amountRest =  (amount % eventMembers.size());
        int amountToDivideEvenly = amount - amountRest;
        int amountPerPerson = amountToDivideEvenly / eventMembers.size();

        List<ExpenditurePerMember> expenditurePerMemberList = new ArrayList<>();
        ArrayList<EventMember> eventMembersArrayList = new ArrayList<>(eventMembers);
        Collections.shuffle(eventMembersArrayList);

        int i = 0;
        for (EventMember eventMember : eventMembersArrayList) {
            ExpenditurePerMember expenditurePerMember = new ExpenditurePerMember();
            int personalAmount = amountPerPerson;

            if (i < amountRest) {
                personalAmount += 1;
                i += 1;
            }

            expenditurePerMember.setUsername(eventMember.getUsername());
            expenditurePerMember.setFirstName(eventMember.getFirstName());
            expenditurePerMember.setLastName(eventMember.getLastName());
            expenditurePerMember.setAmount(personalAmount);

            expenditurePerMemberList.add(expenditurePerMember);
        }

        expenditurePerMemberList.sort(Comparator.comparing(ExpenditurePerMember::getUsername));
        return expenditurePerMemberList;
    }

    public List<EventMember> setNewBalance(
            List<EventMember> eventMembers, List<ExpenditurePerMember> expenditurePerMembers, UserDto payer, int amount) {
        for (EventMember eventMember : eventMembers) {
            for (ExpenditurePerMember expenditurePerMember : expenditurePerMembers) {
                if (eventMember.getUsername().equals(expenditurePerMember.getUsername())){
                    eventMember.setBalance(eventMember.getBalance() - expenditurePerMember.getAmount());
                    if (eventMember.getUsername().equals(payer.getUsername())) {
                        eventMember.setBalance(eventMember.getBalance() + amount);
                    }
                    break;
                }
            }
        }
        return eventMembers;
    }

}