package taskreminder.dto;
import java.time.LocalDateTime;

public class ReminderDTO {
	
	    private String title;
	    private String description;
	    private LocalDateTime dueDate;
	    private Integer notifyBeforeHours;
	    private String priority;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public LocalDateTime getDueDate() {
			return dueDate;
		}
		public void setDueDate(LocalDateTime dueDate) {
			this.dueDate = dueDate;
		}
		public Integer getNotifyBeforeHours() {
			return notifyBeforeHours;
		}
		public void setNotifyBeforeHours(Integer notifyBeforeHours) {
			this.notifyBeforeHours = notifyBeforeHours;
		}
		public String getPriority() {
			return priority;
		}
		public void setPriority(String priority) {
			this.priority = priority;
		}
	}