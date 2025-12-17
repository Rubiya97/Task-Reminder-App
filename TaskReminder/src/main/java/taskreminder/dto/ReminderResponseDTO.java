package taskreminder.dto;
import java.time.LocalDateTime;

public class ReminderResponseDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Integer notifyBeforeHours;
    private String priority;
    private Boolean uiNotified;
    
    // Constructor
    public ReminderResponseDTO(Long id, String title, String description, LocalDateTime dueDate,
                               Integer notifyBeforeHours, String priority, Boolean uiNotified) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.notifyBeforeHours = notifyBeforeHours;
        this.priority = priority;
        this.uiNotified = uiNotified;
    }
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public Boolean getUiNotified() {
		return uiNotified;
	}
	public void setUiNotified(Boolean uiNotified) {
		this.uiNotified = uiNotified;
	}
}