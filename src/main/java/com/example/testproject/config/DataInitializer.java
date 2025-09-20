package com.example.testproject.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.testproject.entity.Role;
import com.example.testproject.entity.User;
import com.example.testproject.repo.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Create test users
        if (userRepository.count() == 0) {
            // Maker users
            userRepository.save(new User("maker1", "password123", Set.of(Role.MAKER)));
            userRepository.save(new User("maker2", "password123", Set.of(Role.MAKER)));

            // Checker users
            userRepository.save(new User("checker1", "password123", Set.of(Role.CHECKER)));
            userRepository.save(new User("checker2", "password123", Set.of(Role.CHECKER)));

            // Admin user (both roles)
            userRepository.save(new User("admin", "admin123",
                    Set.of(Role.ADMIN, Role.MAKER, Role.CHECKER)));

            System.out.println("Test users created:");
            System.out.println("Makers: maker1, maker2");
            System.out.println("Checkers: checker1, checker2");
            System.out.println("Admin: admin");
        }
    }
}
