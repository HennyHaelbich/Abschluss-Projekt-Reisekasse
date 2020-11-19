package de.neuefische.henny.reisekasse.Service;

import de.neuefische.henny.reisekasse.Db.EventDb;
import de.neuefische.henny.reisekasse.utils.IdUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {
    final EventDb eventDb = mock(EventDb.class);
    final IdUtils idUtils = mock(IdUtils.class);


    @Test
    void addEvent() {
    }
}