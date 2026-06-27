package com.sams.config;

import com.sams.entity.Role;
import com.sams.entity.User;
import com.sams.enums.RoleName;
import com.sams.repository.RoleRepository;
import com.sams.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        Role adminRole = createRole(RoleName.ADMIN);
        Role facultyRole = createRole(RoleName.FACULTY);
        Role studentRole = createRole(RoleName.STUDENT);

        createUser(
                "Admin",
                "admin@sams.edu",
                "password",
                adminRole
        );

        createUser(
                "Priya Sharma",
                "priya.sharma@sams.edu",
                "password",
                facultyRole
        );

        createUser(
                "Arjun Patel",
                "arjun.patel@sams.edu",
                "password",
                studentRole
        );

        System.out.println("====================================");
        System.out.println("Demo users initialized successfully");
        System.out.println("====================================");
    }

    private Role createRole(RoleName roleName) {

        return roleRepository.findByName(roleName)
                .orElseGet(() -> {

                    Role role = new Role();
                    role.setName(roleName);

                    return roleRepository.save(role);
                });
    }

    private void createUser(String name,
                            String email,
                            String password,
                            Role role) {

        if (userRepository.existsByEmail(email)) {
            return;
        }

        User user = new User();

        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhone("9999999999");
        user.setRole(role);
        user.setIsActive(true);

        userRepository.save(user);

        System.out.println("Created user: " + email);
    }
}
