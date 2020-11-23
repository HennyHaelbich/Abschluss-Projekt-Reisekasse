package de.neuefische.henny.reisekasse.model;

import de.neuefische.henny.reisekasse.model.dto.UserDto;
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
@Document(collection = "expenditures")
public class Expenditures {
    @Id
    private String id;
    private List<UserDto> members;
    private de.neuefische.henny.reisekasse.model.User payer;
    private Double amount;

}
