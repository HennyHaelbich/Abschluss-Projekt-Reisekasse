package de.neuefische.henny.reisekasse.Db;

import de.neuefische.henny.reisekasse.Model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserDb extends PagingAndSortingRepository<User, String> {
    List<User> findAll();

    Optional<User> findById(String username);
}
