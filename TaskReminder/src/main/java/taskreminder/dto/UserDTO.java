package taskreminder.dto;

import jakarta.validation.constraints.NotEmpty;




public class UserDTO {

    @NotEmpty(message = "Invalid: username cannot be empty")
    private String username;

    @NotEmpty(message = "Invalid: email cannot be empty")
    private String email;

    @NotEmpty(message = "Invalid: password cannot be empty")
    private String password;

    @NotEmpty(message = "Invalid: phone number cannot be empty")
    private String phoneNumber;

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

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}