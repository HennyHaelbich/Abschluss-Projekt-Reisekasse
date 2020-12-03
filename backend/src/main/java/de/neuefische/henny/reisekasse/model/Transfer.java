package de.neuefische.henny.reisekasse.model;

import de.neuefische.henny.reisekasse.model.dto.UserIdDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transfer {
    private UserIdDto payer;
    private UserIdDto paymentReceiver;
    private int amount;
}
