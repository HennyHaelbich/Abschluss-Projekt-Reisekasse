package de.neuefische.henny.reisekasse.Model.Dto;

import de.neuefische.henny.reisekasse.Model.Expenditures;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddEventDto {
    private String Title;
    private List<UserDto> members;
    private List<Expenditures> expenditures;
}