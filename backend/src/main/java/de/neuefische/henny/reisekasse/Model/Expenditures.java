package de.neuefische.henny.reisekasse.Model;

import de.neuefische.henny.reisekasse.Model.Dto.UserDto;
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
    private User payer;
    private Double amount;

}
