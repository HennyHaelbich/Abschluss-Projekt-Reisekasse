package de.neuefische.henny.reisekasse.Service;

import de.neuefische.henny.reisekasse.Db.UserDb;
import de.neuefische.henny.reisekasse.Model.Dto.UserDto;
import de.neuefische.henny.reisekasse.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserDb userDb;

    @Autowired
    public UserService(UserDb userDb) {
        this.userDb = userDb;
    }

    public List<UserDto> getUsers(){
        List<User> users = userDb.findAll();
        return users.stream().map(user -> new UserDto(user.getUsername())).collect(Collectors.toList());
    }

    public Optional<User> getUserById(String username){
        return userDb.findById(username);
    }

}
