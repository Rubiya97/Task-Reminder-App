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
    
  /*  @Scheduled(fixedRate = 30000)
    public void checkReminders() {

        LocalDateTime now = LocalDateTime.now();
        System.out.println("⏳ Scheduler running at: " + now);

        List<Reminder> reminders = reminderRepository.findAll();
        System.out.println("📌 Total reminders: " + reminders.size());

        for (Reminder r : reminders) {
            System.out.println("---- Checking reminder: " + r.getTitle() + " ----");
            System.out.println("due = " + r.getDueDate());
            System.out.println("notifyBeforeHours = " + r.getNotifyBeforeHours());
            System.out.println("notified = " + r.isNotified());

            if (r.getDueDate() == null || r.getNotifyBeforeHours() == null) {
                System.out.println("❌ Skipping because dueDate or notifyBeforeHours is null");
                continue;
            }

            Boolean notified = r.isNotified();
            if (notified == null) notified = false;

            if (notified) {
                System.out.println("✔ Already notified. Skipping.");
                continue;
            }

            LocalDateTime due = r.getDueDate();
            long notifyBeforeHours = r.getNotifyBeforeHours();
            LocalDateTime notifyTime = due.minusHours(notifyBeforeHours);

            System.out.println("notifyTime = " + notifyTime);

            if (!now.isBefore(notifyTime)) {
                System.out.println("📧 Sending email now...");
                sendNotification(r);
            } else {
                System.out.println("⏱ Not time yet.");
            }
        }
    }*/


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

            // ------------- EDGE CASE #1 ----------------
            // If notify time is in the past (user created overdue or near-due reminder)
            // → Notify immediately
            if (!now.isBefore(notifyTime)) {
                sendNotification(r);
            }
        }
    }

    private void sendNotification(Reminder r) {
        try {
        	 System.out.println("📨 Attempting email to: " + r.getUser().getEmail());
            //String subject = r.getNotifyBeforeHours() + "h Reminder";
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
