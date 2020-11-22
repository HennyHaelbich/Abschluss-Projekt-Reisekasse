package de.neuefische.henny.reisekasse.model.dto;

import de.neuefische.henny.reisekasse.model.EventMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
