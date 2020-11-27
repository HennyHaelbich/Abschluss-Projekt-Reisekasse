package de.neuefische.henny.reisekasse.model;

import de.neuefische.henny.reisekasse.model.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "expenditures")
public class Expenditure {
    @Id
    private String id;
    private String description;
    private List<UserDto> members;
    private TravelFoundUser payer;
    private Double amount;
    private Instant timestamp;
}
