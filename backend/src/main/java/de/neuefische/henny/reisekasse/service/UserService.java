package de.neuefische.henny.reisekasse.service;

import de.neuefische.henny.reisekasse.model.TravelFoundUser;
import de.neuefische.henny.reisekasse.db.UserDb;
import de.neuefische.henny.reisekasse.model.dto.AddTravelFundUserDto;
import de.neuefische.henny.reisekasse.model.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService {
    private final UserDb userDb;

    @Autowired
    public UserService(UserDb userDb) {
        this.userDb = userDb;
    }

    public Optional<UserDto> getUserById(String username) {
        Optional<TravelFoundUser> optionalUser = userDb.findById(username);
        if(optionalUser.isEmpty()) {
            return Optional.empty();
        }
        UserDto userWithoutPassword = new UserDto(optionalUser.get().getUsername(), optionalUser.get().getFirstName(), optionalUser.get().getLastName());
        return Optional.of(userWithoutPassword);
    }

    public TravelFoundUser registerNewUser(AddTravelFundUserDto addTravelFundUserDto) {
        String passwordBCrypt = new BCryptPasswordEncoder().encode(addTravelFundUserDto.getPassword());

        TravelFoundUser newUser = TravelFoundUser.builder()
                .username(addTravelFundUserDto.getUsername())
                .password(passwordBCrypt)
                .firstName(addTravelFundUserDto.getFirstName())
                .lastName(addTravelFundUserDto.getLastName())
                .build();

        return userDb.save(newUser);
    }

    public boolean passwordIsValid(String password) {
        return passwordIsLongEnough(password) && containsNumbers(password) && containsUpperCase(password) && containsLowerCase(password);
    }

    public boolean passwordIsLongEnough(String password) {
        return password.length() >= 8;
    }

    public boolean containsNumbers(String password) {
        return password.matches(".*\\d.*");
    }

    public boolean containsUpperCase(String password) {
        return password.matches(".*[A-Z].*");
    }

    public boolean containsLowerCase(String password) {
        return password.matches(".*[a-z].*");
    }

}
