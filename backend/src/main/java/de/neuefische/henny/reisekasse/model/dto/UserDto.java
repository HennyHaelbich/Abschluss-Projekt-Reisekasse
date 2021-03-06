package de.neuefische.henny.reisekasse.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String username;
    private String firstName;
    private String lastName;

    public UserDto(String username) {
        this.username = username;
    }
}
