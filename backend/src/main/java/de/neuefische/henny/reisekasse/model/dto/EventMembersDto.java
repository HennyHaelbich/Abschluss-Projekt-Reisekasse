package de.neuefische.henny.reisekasse.model.dto;

import de.neuefische.henny.reisekasse.model.Event;
import de.neuefische.henny.reisekasse.model.EventMember;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventMembersDto {
    private List<EventMember> members;
}
