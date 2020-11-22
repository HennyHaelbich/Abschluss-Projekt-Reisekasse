package de.neuefische.henny.reisekasse.Model.Dto;

import de.neuefische.henny.reisekasse.Model.EventMember;
import de.neuefische.henny.reisekasse.Model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddExpenditureDto {
    private String eventId;
    private List<EventMember> members;
    private UserDto payer;
    private double amount;

}
