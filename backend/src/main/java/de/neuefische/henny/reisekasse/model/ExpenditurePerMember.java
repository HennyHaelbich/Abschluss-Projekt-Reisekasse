package de.neuefische.henny.reisekasse.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenditurePerMember {

    private String username;
    private String firstName;
    private String lastName;
    private int amount;

    public String getName(){
        return getFirstName()+" "+getLastName();
    }
}
