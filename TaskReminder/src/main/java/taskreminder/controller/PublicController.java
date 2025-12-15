package taskreminder.controller;


import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import net.engineeringdigest.journalApp.dto.UserLoginDTO;
import net.engineeringdigest.journalApp.dto.UserResponseDTO;


@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Ok";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDTO userDTO) {
        User newUser = new User();
        newUser.setUsername(userDTO.getUsername());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        newUser.setPhoneNumber(userDTO.getPhoneNumber());
        String s = userService.saveNewUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(s);
    }
    
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponseDTO>> login(@RequestBody UserLoginDTO dto) {
        try {
            String username = dto.getUsername();
            String password = dto.getPassword();

            // Find user by username
            User dbUser = userService.findByUsername(username);
            if (dbUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, null, "Incorrect username or password"));
            }

            // Validate password
            if (!passwordEncoder.matches(password, dbUser.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, null, "Incorrect username or password"));
            }

            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // Store authentication in SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);


            // Create JWT
            String jwt = jwtUtil.generateToken(username);

            // Build response DTO
            UserResponseDTO response = new UserResponseDTO(
                    jwt, dbUser.getUser_id(), dbUser.getUsername(), dbUser.getEmail()
            );

            // Return success response
            return ResponseEntity.ok(new ApiResponse<>(true, response, "Login successful"));

        } catch (Exception e) {
            // Return failure response with message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, null, "Login failed: " + e.getMessage()));
        }
    }


    
    @PostMapping("/create")
    public ResponseEntity<?> createSimpleUser() {
        String rawPassword = "123"; // Simple password
        System.out.println("=== CREATING SIMPLE USER ===");
        System.out.println("Raw password: '" + rawPassword + "'");
        
        String encodedPassword = passwordEncoder.encode(rawPassword);
        System.out.println("Encoded password: " + encodedPassword);
        
        User testUser = new User();
        testUser.setUsername("simpleuser123");
        testUser.setEmail("simple123@test.com");
        testUser.setPassword(encodedPassword);
        testUser.setPhoneNumber("1234567890");
        testUser.setRoles("USER");
        
       String saved=userService.saveNewUser(testUser);
        System.out.println("Save successful: " + saved);
        
        return ResponseEntity.ok("Simple user created:\n" +
                "Username: simpleuser\n" +
                "Password: 123\n" +
                "Encoded: " + encodedPassword);
    }
    
    @PostMapping("/debug")
    public ResponseEntity<?> debugPassword(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        
        System.out.println("=== DEBUG PASSWORD ===");
        System.out.println("Username: " + username);
        System.out.println("Password to test: " + password);
        
        // Find user
        Optional<User> userOpt = Optional.ofNullable(userService.findByUsername(username));
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        
        User user = userOpt.get();
        System.out.println("Stored hash: " + user.getPassword());
        
        // Test multiple encoding scenarios
        String encodedNow = passwordEncoder.encode(password);
        System.out.println("Encoded now: " + encodedNow);
        
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        System.out.println("Password matches: " + matches);
        
        // Test if it matches the new encoding
        boolean matchesNew = passwordEncoder.matches(password, encodedNow);
        System.out.println("Matches new encoding: " + matchesNew);
        
        return ResponseEntity.ok(Map.of(
            "storedHash", user.getPassword(),
            "newHash", encodedNow,
            "matches", matches,
            "matchesNew", matchesNew
        ));
    }
    @GetMapping("/check")
    public ResponseEntity<?> checkEncoder() {
        String password = "test123";
        String encoded1 = passwordEncoder.encode(password);
        String encoded2 = passwordEncoder.encode(password);
        
        boolean matches1 = passwordEncoder.matches(password, encoded1);
        boolean matches2 = passwordEncoder.matches(password, encoded2);
        
        System.out.println("Encoded 1: " + encoded1);
        System.out.println("Encoded 2: " + encoded2);
        System.out.println("Matches 1: " + matches1);
        System.out.println("Matches 2: " + matches2);
        System.out.println("Same instance: " + (passwordEncoder == passwordEncoder));
        
        return ResponseEntity.ok(Map.of(
            "encoded1", encoded1,
            "encoded2", encoded2,
            "matches1", matches1,
            "matches2", matches2,
            "hashesDifferent", !encoded1.equals(encoded2),
            "bothMatch", matches1 && matches2
        ));
    }
    
    @PostMapping("/test")
    public ResponseEntity<?> testWhitespace(@RequestBody Map<String, String> request) {
        String password = request.get("password");
        
        System.out.println("=== WHITESPACE TEST ===");
        System.out.println("Original: '" + password + "'");
        System.out.println("Trimmed: '" + password.trim() + "'");
        System.out.println("Length original: " + password.length());
        System.out.println("Length trimmed: " + password.trim().length());
        System.out.println("Bytes: " + Arrays.toString(password.getBytes()));
        
        return ResponseEntity.ok(Map.of(
            "original", password,
            "trimmed", password.trim(),
            "lengthOriginal", password.length(),
            "lengthTrimmed", password.trim().length(),
            "equalsTrimmed", password.equals(password.trim())
        ));
    }
    
}

