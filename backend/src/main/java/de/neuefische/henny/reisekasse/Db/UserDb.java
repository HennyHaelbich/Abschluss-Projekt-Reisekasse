package de.neuefische.henny.reisekasse.Db;

import de.neuefische.henny.reisekasse.Model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserDb extends PagingAndSortingRepository<User, String> {
}
