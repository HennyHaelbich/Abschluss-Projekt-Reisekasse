package de.neuefische.henny.reisekasse.controller;

import de.neuefische.henny.reisekasse.model.TravelFoundUser;
import de.neuefische.henny.reisekasse.model.dto.AddTravelFundUserDto;
import de.neuefische.henny.reisekasse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth/registration")
public class RegistrationController {
    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
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

    /*
    @PostMapping
    public AddTravelFundUserDto signUp(@RequestBody AddTravelFundUserDto addTravelFoundUserDto){
        return addTravelFoundUserDto;
    }
     */
}
