package de.neuefische.henny.reisekasse.db;

import de.neuefische.henny.reisekasse.model.TravelFoundUser;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserDb extends PagingAndSortingRepository<TravelFoundUser, String> {
    Optional<TravelFoundUser> findById(String username);
}
