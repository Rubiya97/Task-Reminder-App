package taskreminder.service;
import taskreminder.entity.User;


import taskreminder.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;



@Service
public class UserDetailsServiceImpl implements UserDetailsService,UserService {

    @Autowired
    private UserRepository userRepository;
    
    

    
    public String saveNewUser(User user) {
      
    	
    	if (userRepository.existsByEmail(user.getEmail())) {
            return "Email already registered!";
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            return "Username already taken!";
        }
       
        
    	try {
            //user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles("USER"); // single string
            userRepository.save(user);
            return "User registered successfully!";
        } catch (Exception e) {
            return "exception";
        }
    }

    public void saveAdmin(User user) {
        //user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles("USER,ADMIN"); // multiple roles as comma-separated
        userRepository.save(user);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    
    
    
    
    
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        // Split roles by comma
        String[] rolesArray = user.getRoles().split(",");

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(rolesArray)
                .build();
    }
}