package de.neuefische.henny.reisekasse.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddTravelFundUserDto {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
}
