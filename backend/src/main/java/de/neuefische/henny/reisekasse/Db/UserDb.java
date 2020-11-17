package de.neuefische.henny.reisekasse.Db;

import de.neuefische.henny.reisekasse.Model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UserDb extends PagingAndSortingRepository<User, String> {
    List<User> findAll();
}
