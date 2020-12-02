package de.neuefische.henny.reisekasse.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdsDto {
    private String eventId;
    private String expenditureId;
}
