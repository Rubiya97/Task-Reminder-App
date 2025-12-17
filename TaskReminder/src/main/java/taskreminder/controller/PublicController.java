package taskreminder.controller;

import taskreminder.dto.ApiResponse;

import taskreminder.dto.UserDTO;

import taskreminder.entity.User;
import taskreminder.service.UserDetailsServiceImpl;
import taskreminder.service.UserService;
import taskreminder.jwt.JwtUtil;
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
import taskreminder.dto.UserLoginDTO;
import taskreminder.dto.UserResponseDTO;
import taskreminder.dto.UserDTO;
import taskreminder.dto.ApiResponse;


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

    
}

