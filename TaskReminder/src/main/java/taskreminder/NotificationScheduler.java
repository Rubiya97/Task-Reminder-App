package taskreminder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import taskreminder.entity.Reminder;
import taskreminder.repository.ReminderRepository;
import taskreminder.service.EmailService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class NotificationScheduler {

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private EmailService emailService;
    

    @Scheduled(fixedRate = 30000) // every 30 seconds
    public void checkReminders() {

     LocalDateTime now = LocalDateTime.now();


        List<Reminder> reminders = reminderRepository.findAll();

        for (Reminder r : reminders) {

            // Normalize nulls
            if (r.getDueDate() == null || r.getNotifyBeforeHours() == null)
                continue;

            Boolean notified = r.isNotified();
            if (notified == null) notified = false;

            // Already notified → skip
            if (notified) continue;

            LocalDateTime due = r.getDueDate();
            long notifyBeforeHours = r.getNotifyBeforeHours();

            // Calculate when user *should* be notified
            LocalDateTime notifyTime = due.minusHours(notifyBeforeHours);

    
            if (!now.isBefore(notifyTime)) {
                sendNotification(r);
            }
        }
    }

    private void sendNotification(Reminder r) {
        try {
        	 System.out.println("📨 Attempting email to: " + r.getUser().getEmail());
         
        	 String subject= r.getTitle();
            String text = "Your task \"" + r.getTitle() + "\" is due in " +
                    r.getNotifyBeforeHours() + " hours.";

            emailService.sendEmail(r.getUser().getEmail(), subject, text);

            r.setNotified(true);
            reminderRepository.save(r);

        } catch (Exception e) {
        	  e.printStackTrace();
            System.err.println("Failed to send reminder: " + e.getMessage());
        }
    }
}
