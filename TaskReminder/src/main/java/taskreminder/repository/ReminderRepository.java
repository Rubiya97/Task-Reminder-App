package taskreminder.repository;
import taskreminder.entity.Reminder;

import taskreminder.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface ReminderRepository extends JpaRepository<Reminder,Long> {
    List<Reminder> findByUser(User user);
  
    List<Reminder> findByDueDateBetweenAndNotifyBeforeHoursAndNotified(
    	    LocalDateTime start, LocalDateTime end, int notifyBeforeHours, boolean notified
    	);

}