package de.neuefische.henny.reisekasse.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventMember {
    private String username;
    private String firstName;
    private String lastName;
    private int balance;

    public EventMember(String username, int balance) {
        this.username = username;
        this.balance = balance;
    }

    public String getName() {
        return getFirstName()+" "+getLastName();
    }
}
