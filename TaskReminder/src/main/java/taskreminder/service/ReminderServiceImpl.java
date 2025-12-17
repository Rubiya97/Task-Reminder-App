package taskreminder.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import taskreminder.dto.ReminderDTO;
import taskreminder.dto.ReminderResponseDTO;
import taskreminder.entity.Reminder;
import taskreminder.entity.User;

import taskreminder.repository.ReminderRepository;

@Service
public class ReminderServiceImpl implements ReminderService {

	

	    @Autowired
	    private ReminderRepository reminderRepository;

	    @Override
	    public List<Reminder> getMyReminders(User user) {
	        return reminderRepository.findByUser(user);
	    }

	    @Override
	    public ReminderResponseDTO addReminder(ReminderDTO dto, User user) {
	        Reminder reminder = new Reminder();

	        reminder.setTitle(dto.getTitle());
	        reminder.setDescription(dto.getDescription());
	        reminder.setDueDate(dto.getDueDate());
	        reminder.setNotifyBeforeHours(dto.getNotifyBeforeHours());
	        reminder.setPriority(dto.getPriority());
	        reminder.setUiNotified(false);  // default for new reminders
	        reminder.setUser(user);

	        Reminder saved = reminderRepository.save(reminder);

	        // Return a DTO for the frontend
	        return new ReminderResponseDTO(
	            saved.getId(),
	            saved.getTitle(),
	            saved.getDescription(),
	            saved.getDueDate(),
	            saved.getNotifyBeforeHours(),
	            saved.getPriority(),
	            saved.getUiNotified()
	        );
	    }

	}


