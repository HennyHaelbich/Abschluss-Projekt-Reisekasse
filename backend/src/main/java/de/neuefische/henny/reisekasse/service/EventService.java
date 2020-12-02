package de.neuefische.henny.reisekasse.service;

import de.neuefische.henny.reisekasse.db.EventDb;
import de.neuefische.henny.reisekasse.model.*;
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

import java.util.*;
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

        Event event = getEventById(eventId);

        event.getExpenditures().add(newExpenditure);

        List<EventMember> updatedEventMembers =
                updateBalance(addExpenditureDto.getMembers(), expenditurePerMemberList, addExpenditureDto.getPayer(), addExpenditureDto.getAmount(), true);

        event.setMembers(updatedEventMembers);

        return eventDb.save(event);

    }

    public Event deleteExpenditure(String eventId, String expenditureId) {

        Event event = getEventById(eventId);

        Optional<Expenditure> optionalExpenditureToDelete = event.getExpenditures().stream()
                .filter(expenditure -> expenditure.getId().equals(expenditureId))
                .findAny();

        List<Expenditure> ExpendituresWithoutToDeleteExpenditure = event.getExpenditures().stream()
                .filter(expenditure -> !expenditure.getId().equals(expenditureId))
                .collect(Collectors.toList());

        if(optionalExpenditureToDelete.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "expenditure not found");
        }

        Expenditure expenditureToDelete = optionalExpenditureToDelete.get();

        List<EventMember> updatedEventMembers = updateBalance(
                event.getMembers(), expenditureToDelete.getExpenditurePerMemberList(), expenditureToDelete.getPayer(), expenditureToDelete.getAmount(), false);

        event.setMembers(updatedEventMembers);
        event.setExpenditures(ExpendituresWithoutToDeleteExpenditure);

        return eventDb.save(event);
    }


    public Event getEventById(String eventId) {
        return eventDb.findById(eventId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "event not found"));
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

    public List<EventMember> updateBalance(
            List<EventMember> eventMembers, List<ExpenditurePerMember> expenditurePerMembers, UserDto payer, int amount, boolean addExpenditure) {
        for (EventMember eventMember : eventMembers) {
            for (ExpenditurePerMember expenditurePerMember : expenditurePerMembers) {
                if (eventMember.getUsername().equals(expenditurePerMember.getUsername())){
                    if (addExpenditure) {
                        eventMember.setBalance(eventMember.getBalance() - expenditurePerMember.getAmount());
                    } else {
                        eventMember.setBalance(eventMember.getBalance() + expenditurePerMember.getAmount());
                    }
                    if (eventMember.getUsername().equals(payer.getUsername())) {
                        if(addExpenditure) {
                            eventMember.setBalance(eventMember.getBalance() + amount);
                        } else {
                            eventMember.setBalance(eventMember.getBalance() - amount);
                        }
                    }
                    break;
                }
            }
        }
        return eventMembers;
    }

    public List<Transfer> compensateBalances(List<EventMember> eventMembers) {
        List<EventMember> payers = eventMembers.stream()
                .filter(member -> member.getBalance() < 0)
                .collect(Collectors.toList());
        payers.sort(Comparator.comparing(EventMember::getBalance));

        List<EventMember> paymentReceiver = eventMembers.stream()
                .filter(member -> member.getBalance() > 0)
                .collect(Collectors.toList());
        paymentReceiver.sort(Comparator.comparing(EventMember::getBalance));

        List<Transfer> transferList = new ArrayList<>();


    if (paymentReceiver.get(0).getBalance() <= Math.abs(payers.get(0).getBalance())){

        Transfer transfer = new Transfer(new UserDto(payers.get(0).getUsername(), payers.get(0).getFirstName(), payers.get(0).getLastName()),
                new UserDto(payers.get(0).getUsername(), payers.get(0).getFirstName(), payers.get(0).getLastName()),
                paymentReceiver.get(0).getBalance());
        transferList.add(transfer);

        payers.get(0).setBalance(payers.get(0).getBalance() + paymentReceiver.get(0).getBalance());
        paymentReceiver.remove(0);

        if(payers.get(0).getBalance() == 0){
            payers.remove(0);
        }



        }

        return null;
    }

}