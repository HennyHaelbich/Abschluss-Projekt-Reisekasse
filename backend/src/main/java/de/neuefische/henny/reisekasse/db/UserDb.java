package de.neuefische.henny.reisekasse.db;

import de.neuefische.henny.reisekasse.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserDb extends PagingAndSortingRepository<User, String> {
}