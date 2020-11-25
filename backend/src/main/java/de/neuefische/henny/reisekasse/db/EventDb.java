package de.neuefische.henny.reisekasse.db;

import de.neuefische.henny.reisekasse.model.Event;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface EventDb extends PagingAndSortingRepository<Event, String> {
    List<Event> findAll();
}
