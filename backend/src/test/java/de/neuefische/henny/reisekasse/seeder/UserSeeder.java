package de.neuefische.henny.reisekasse.seeder;

import de.neuefische.henny.reisekasse.Model.User;

import java.util.ArrayList;
import java.util.List;

public class UserSeeder {
    private static final List<User>  users = new ArrayList<>(List.of(
            User.builder().username("Malte").build(),
            User.builder().username("Sven").build(),
            User.builder().username("Dennis").build(),
            User.builder().username("Nil").build()
    ));

    public static List<User> getStockUser(){
        return users;
    }
}
