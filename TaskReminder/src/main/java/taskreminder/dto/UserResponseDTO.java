package taskreminder.dto;

public class UserResponseDTO {
	
    private Integer user_id;
    private String username;
    private String email;
    private String token;
    private String phoneNumber;
    
    public UserResponseDTO(String token,Integer user_id, String username, String email) {
    	this.user_id=user_id;
        this.setToken(token);
        this.username = username;
        this.email = email;
    }
    
    
    public UserResponseDTO(String phoneNumber, String username, String email) {
    	//this.user_id=user_id;
       this.phoneNumber=phoneNumber;
        this.username = username;
        this.email = email;
    }
    
    
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}