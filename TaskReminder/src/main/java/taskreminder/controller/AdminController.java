package taskreminder.controller;




import taskreminder.entity.User;

import taskreminder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> all = userService.getAll();
        if (all != null && !all.isEmpty()) {
            return ResponseEntity.ok(all);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create-admin-user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        userService.saveAdmin(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



    
}