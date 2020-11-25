package de.neuefische.henny.reisekasse.service;


import de.neuefische.henny.reisekasse.model.TravelFoundUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final de.neuefische.henny.reisekasse.db.UserDb userDb;

    @Autowired
    public UserService(de.neuefische.henny.reisekasse.db.UserDb userDb) {
        this.userDb = userDb;
    }

    public Optional<TravelFoundUser> getUserById(String username){
        return userDb.findById(username);
    }

}
