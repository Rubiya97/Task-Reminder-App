package taskreminder.controller;



import taskreminder.dto.UserResponseDTO;
import taskreminder.entity.User;

import taskreminder.repository.UserRepository;
import taskreminder.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User userInDb = userService.findByUsername(username);
        userInDb.setUsername(user.getUsername());
        userInDb.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveNewUser(userInDb);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUserById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUsername(authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<String> greeting() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Hi " + authentication.getName());
    }
    
    @GetMapping("/profile")
    public UserResponseDTO profile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.findByUsername(username);

        return new UserResponseDTO(user.getPhoneNumber(), user.getUsername(), user.getEmail());
    }

}
