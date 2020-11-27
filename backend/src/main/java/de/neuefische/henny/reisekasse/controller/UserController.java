package de.neuefische.henny.reisekasse.controller;

import de.neuefische.henny.reisekasse.model.dto.AddTravelFundUserDto;
import de.neuefische.henny.reisekasse.model.dto.UserDto;
import de.neuefische.henny.reisekasse.model.TravelFoundUser;
import de.neuefische.henny.reisekasse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{username}")
    public UserDto getUserById(@PathVariable @NonNull String username) {
        Optional<TravelFoundUser> optionalUser = userService.getUserById(username);
        if(optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new UserDto(optionalUser.get().getUsername());
    }

    @PostMapping
    public String signUp(@RequestBody AddTravelFundUserDto addTravelFoundUserDto){
        if(!userService.passwordIsValid(addTravelFoundUserDto.getPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password not valid");
        }
        if(userService.getUserById(addTravelFoundUserDto.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user already exists");
        }

        TravelFoundUser newUser = userService.registerNewUser(addTravelFoundUserDto);
        return newUser.getFirstName();
    }

}
