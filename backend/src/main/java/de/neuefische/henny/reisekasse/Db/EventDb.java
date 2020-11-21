package de.neuefische.henny.reisekasse.Db;

import de.neuefische.henny.reisekasse.Model.Event;
import de.neuefische.henny.reisekasse.Model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface EventDb extends PagingAndSortingRepository<Event, String> {
    List<Event> findAll();
}
