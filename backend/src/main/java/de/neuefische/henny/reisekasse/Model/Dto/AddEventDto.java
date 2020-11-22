package de.neuefische.henny.reisekasse.Model.Dto;

import de.neuefische.henny.reisekasse.Model.Expenditure;
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
    private List<Expenditure> expenditures;
}