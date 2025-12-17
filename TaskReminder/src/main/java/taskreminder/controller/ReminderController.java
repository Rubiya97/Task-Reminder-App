package taskreminder.controller;



import java.util.List;


import java.util.Optional;
import java.util.stream.Collectors;

import taskreminder.entity.Reminder;
import taskreminder.service.ReminderService;

import taskreminder.repository.*;
import  taskreminder.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;



import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import taskreminder.dto.ReminderResponseDTO;
import taskreminder.dto.ReminderDTO;

@RestController
@RequestMapping("/reminders")
public class ReminderController {

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ReminderService reminderService;

    // Get currently logged-in user from JWT token
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUsername(username);
    }

    @PostMapping("/add")
    public ReminderResponseDTO addReminder(@RequestBody ReminderDTO reminderDTO) {
        User currentUser = getCurrentUser();
        return reminderService.addReminder(reminderDTO, currentUser);
    }


    // GET MY REMINDERS 
    @GetMapping("/mine")
    public List<ReminderResponseDTO> getMyReminders(Authentication authentication) {
    	  UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    	  String username = userDetails.getUsername();

    	    // Load your actual User entity from DB
    	    User currentUser = userRepository.findByUsername(username);

    	    List<Reminder> reminders = reminderService.getMyReminders(currentUser);

    	    return reminders.stream()
    	            .map(reminder -> new ReminderResponseDTO(
    	                    reminder.getId(),
    	                    reminder.getTitle(),
    	                    reminder.getDescription(),
    	                    reminder.getDueDate(),
    	                    reminder.getNotifyBeforeHours(),
    	                    reminder.getPriority(),
    	                    reminder.getUiNotified()
    	            ))
    	            .collect(Collectors.toList());
    }
    
    
    //notification via email
    @PatchMapping("/ui-notified/{id}")
    public Reminder markUiNotified(@PathVariable Long id) {

        User currentUser = getCurrentUser();

        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reminder not found"));

        // Check ownership
        if (!reminder.getUser().getUser_id().equals(currentUser.getUser_id())) {
            throw new RuntimeException("Unauthorized access");
        }

        reminder.setUiNotified(true);
        return reminderRepository.save(reminder);
    }

    
    //update reminder
    @PutMapping("/{id}")
    public Reminder updateReminder(@PathVariable Long id,
                                   @RequestBody ReminderDTO updatedReminder) {

        User currentUser = getCurrentUser();

        Reminder existing = reminderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reminder not found"));

        if (!existing.getUser().getUser_id().equals(currentUser.getUser_id())) {
            throw new RuntimeException("Unauthorized access");
        }

        existing.setTitle(updatedReminder.getTitle());
        existing.setDescription(updatedReminder.getDescription());
        existing.setDueDate(updatedReminder.getDueDate());
        existing.setPriority(updatedReminder.getPriority());
        existing.setNotifyBeforeHours(updatedReminder.getNotifyBeforeHours());

        return reminderRepository.save(existing);
    }
    
    //delete reminder
    @DeleteMapping("/{id}")
    public String deleteReminder(@PathVariable Long id) {

        User currentUser = getCurrentUser();

        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reminder not found"));

        if (!reminder.getUser().getUser_id().equals(currentUser.getUser_id())) {
            throw new RuntimeException("Unauthorized access");
        }

        reminderRepository.delete(reminder);
        return "Reminder deleted successfully";
    }

    //view a specififc reminder
    @GetMapping("/{id}")
    public ReminderResponseDTO viewReminder(@PathVariable Long id) {

        User currentUser = getCurrentUser();

        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reminder not found"));

        if (!reminder.getUser().getUser_id().equals(currentUser.getUser_id())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized access");
        }

        return new ReminderResponseDTO(
                reminder.getId(),
                reminder.getTitle(),
                reminder.getDescription(),
                reminder.getDueDate(),
                reminder.getNotifyBeforeHours(),
                reminder.getPriority(),
                reminder.getUiNotified()
        );
    }
   
}
