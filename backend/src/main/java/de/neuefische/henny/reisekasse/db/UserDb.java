package de.neuefische.henny.reisekasse.db;

import de.neuefische.henny.reisekasse.model.TravelFoundUser;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserDb extends PagingAndSortingRepository<TravelFoundUser, String> {
}