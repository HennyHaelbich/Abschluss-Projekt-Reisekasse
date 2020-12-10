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
    private final UserService userService;

    @Autowired
    public EventService(EventDb eventDb, IdUtils idUtils, TimestampUtils timestampUtils, MongoTemplate mongoTemplate, UserService userService) {
        this.eventDb = eventDb;
        this.idUtils = idUtils;
        this.timestampUtils = timestampUtils;
        this.mongoTemplate = mongoTemplate;
        this.userService = userService;
    }


    public Event addEvent(AddEventDto addEventDto) {

        List<EventMember> eventMembers = addEventDto.getMembers().stream()
                .map(member -> EventMember.builder()
                        .username(member.getUsername())
                        .firstName(member.getFirstName())
                        .lastName(member.getLastName())
                        .balance(0)
                        .build())
                .sorted(Comparator.comparing(EventMember::getName))
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

        UserDto payer = userService.getUserById(addExpenditureDto.getPayerId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Expenditure newExpenditure = Expenditure.builder()
                .id(idUtils.generateId())
                .timestamp(timestampUtils.generateTimestampEpochSeconds())
                .description(addExpenditureDto.getDescription())
                .expenditurePerMemberList(expenditurePerMemberList)
                .payer(payer)
                .amount(addExpenditureDto.getAmount())
                .build();

        Event event = getEventById(eventId);

        event.getExpenditures().add(0, newExpenditure);


        List<EventMember> updatedEventMembers = calculateBalance(event.getMembers(), event.getExpenditures());

        event.setMembers(updatedEventMembers);

        return eventDb.save(event);

    }

    public Event deleteExpenditure(String eventId, String expenditureId) {
        Event event = getEventById(eventId);

        List<Expenditure> expendituresWithoutExpenditureToDelete = event.getExpenditures().stream()
                .filter(expenditure -> !expenditure.getId().equals(expenditureId))
                .collect(Collectors.toList());

        List<EventMember> updatedEventMembers = calculateBalance(event.getMembers(), expendituresWithoutExpenditureToDelete);

        event.setMembers(updatedEventMembers);
        event.setExpenditures(expendituresWithoutExpenditureToDelete);

        return eventDb.save(event);
    }


    public Event getEventById(String eventId) {
        return eventDb.findById(eventId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "event not found"));
    }


    public List<ExpenditurePerMember> calculateExpenditurePerMember(List<EventMember> eventMembers, int amount) {
        int amountRest = (amount % eventMembers.size());
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

        expenditurePerMemberList.sort(Comparator.comparing(ExpenditurePerMember::getName));
        return expenditurePerMemberList;
    }


    public List<EventMember> calculateBalance(List<EventMember> eventMembers, List<Expenditure> expenditureList) {
        for (EventMember eventMember : eventMembers) {
            eventMember.setBalance(0);

            for (Expenditure expenditure : expenditureList) {
                List<ExpenditurePerMember> expenditurePerMemberList = expenditure.getExpenditurePerMemberList();
                UserDto payer = expenditure.getPayer();
                int amount = expenditure.getAmount();

                for (ExpenditurePerMember expenditurePerMember : expenditurePerMemberList) {

                    if (eventMember.getUsername().equals(expenditurePerMember.getUsername())) {
                        eventMember.setBalance(eventMember.getBalance() - expenditurePerMember.getAmount());

                        if (eventMember.getUsername().equals(payer.getUsername())) {
                            eventMember.setBalance(eventMember.getBalance() + amount);
                        }

                        break;
                    }
                }
            }
        }
        return eventMembers;
    }

    public List<Transfer> compensateBalances(List<EventMember> eventMembers) {

        List<EventMember> paymentReceivers = eventMembers.stream()
                .filter(member -> member.getBalance() > 0)
                .sorted(Comparator.comparing(EventMember::getBalance))
                .collect(Collectors.toList());

        List<EventMember> payers = eventMembers.stream()
                .filter(member -> member.getBalance() < 0)
                .sorted(Comparator.comparing(EventMember::getBalance).reversed())
                .collect(Collectors.toList());

        List<Transfer> transferList = new ArrayList<>();

        // Find and handle Subgroups of two
        // to optimise the compensate balances algorithm subgroups of higher order must be found and separated
        for (int j = 0; j < paymentReceivers.size(); j++) {
            EventMember paymentReceiver = paymentReceivers.get(j);
            for (int i = 0; i < payers.size(); i++) {
                EventMember payer = payers.get(i);
                if (paymentReceiver.getBalance() == Math.abs((payer.getBalance()))) {
                    Transfer transfer = Transfer.builder()
                            .payer(new UserDto(payer.getUsername(), payer.getFirstName(), payer.getLastName()))
                            .paymentReceiver(new UserDto(paymentReceiver.getUsername(), paymentReceiver.getFirstName(), paymentReceiver.getLastName()))
                            .amount(paymentReceiver.getBalance())
                            .build();
                    transferList.add(transfer);

                    paymentReceivers.remove(paymentReceiver);
                    payers.remove(payer);
                }
            }
        }
        
        List<Transfer> transferListOfSubgroup = compensateBalancesOfSubgroup(paymentReceivers, payers);
        transferList.addAll(transferListOfSubgroup);
                
        return transferList;
    }

    public List<Transfer> compensateBalancesOfSubgroup(List<EventMember> paymentReceivers, List<EventMember> payers) {
        List<Transfer> transferList = new ArrayList<>();

        while (payers.size() > 0) {
            if (paymentReceivers.get(0).getBalance() <= Math.abs(payers.get(0).getBalance())){

                Transfer transfer = Transfer.builder()
                        .payer(new UserDto(payers.get(0).getUsername(), payers.get(0).getFirstName(), payers.get(0).getLastName()))
                        .paymentReceiver(new UserDto(paymentReceivers.get(0).getUsername(), paymentReceivers.get(0).getFirstName(), paymentReceivers.get(0).getLastName()))
                        .amount(paymentReceivers.get(0).getBalance())
                        .build();
                transferList.add(transfer);

                payers.get(0).setBalance(payers.get(0).getBalance() + paymentReceivers.get(0).getBalance());
                paymentReceivers.remove(0);

                if (payers.get(0).getBalance() == 0) {
                    payers.remove(0);
                }
            } else {
                Transfer transfer = Transfer.builder()
                        .payer(new UserDto(payers.get(0).getUsername(), payers.get(0).getFirstName(), payers.get(0).getLastName()))
                        .paymentReceiver(new UserDto(paymentReceivers.get(0).getUsername(), paymentReceivers.get(0).getFirstName(), paymentReceivers.get(0).getLastName()))
                        .amount(Math.abs(payers.get(0).getBalance()))
                        .build();
                transferList.add(transfer);

                paymentReceivers.get(0).setBalance(payers.get(0).getBalance() + paymentReceivers.get(0).getBalance());
                payers.remove(0);
            }
        }
        return transferList;
    }

}

