package taskreminder.service;
import taskreminder.dto.ReminderDTO;
import taskreminder.dto.ReminderResponseDTO;
import taskreminder.entity.Reminder;
import taskreminder.entity.User;

import java.util.List;

public interface ReminderService {
	List<Reminder> getMyReminders(User user);
	
	 public ReminderResponseDTO addReminder(ReminderDTO reminder, User user);
	 
}
