package de.neuefische.henny.reisekasse.service;

import de.neuefische.henny.reisekasse.model.TravelFoundUser;
import de.neuefische.henny.reisekasse.db.UserDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserDb userDb;

    @Autowired
    public UserService(UserDb userDb) {
        this.userDb = userDb;
    }

    public Optional<TravelFoundUser> getUserById(String username) {
        return userDb.findById(username);
    }

}
