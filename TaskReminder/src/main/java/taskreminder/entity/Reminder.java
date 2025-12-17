package taskreminder.entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    
	private String description;
    private LocalDateTime dueDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    
    private Integer notifyBeforeHours; // 24 or 48
    
    private Boolean notified; // for email backend
    
    private Boolean uiNotified = false; //for frontend UI

    
    private String priority;

    
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
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Boolean isNotified() {
		return notified;
	}
	public void setNotified(boolean notified) {
		this.notified = notified;
	}
	public Integer getNotifyBeforeHours() {
		return notifyBeforeHours;
	}
	public void setNotifyBeforeHours(int notifyBeforeHours) {
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