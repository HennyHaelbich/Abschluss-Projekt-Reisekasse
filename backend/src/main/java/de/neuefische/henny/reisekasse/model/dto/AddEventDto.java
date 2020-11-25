package de.neuefische.henny.reisekasse.model.dto;

import de.neuefische.henny.reisekasse.model.Expenditures;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddEventDto {
    private String title;
    private List<UserDto> members;
    private List<Expenditures> expenditures;
}