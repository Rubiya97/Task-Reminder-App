import React, { useState } from "react";
import { registerApi } from "../service/AuthApiService";
import { useNavigate } from "react-router-dom";
import "../css/tasks.css";
import loginImage from "../assets/login.jpg"; // Import the image

//creating account
const CreateAccount = () => {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [phone_number, setPhonenumber] = useState("");
  const [registerMessage, setRegisterMessage] = useState("");
  const navigate = useNavigate();
  const [errors, setErrors] = useState({
    username: "",
    email: "",
    password: "",
    phone_number: "",
  });

  async function handleRegistrationForm(event) {
    event.preventDefault();
    setRegisterMessage("");

    if (validateForm()) {
      // Map snake_case frontend state to camelCase backend DTO
      const register = { 
        username, 
        email, 
        password, 
        phoneNumber: phone_number 
      };

      try {
        const result = await registerApi(register);
        setRegisterMessage(result); // Shows whatever backend returned

        if (result === "User registered successfully!") {
          setTimeout(() => {
            navigate("/login");
          }, 2000);
        }
      } catch (error) {
        console.error(error);
        setRegisterMessage("Registration failed. Please try again.");
      }
    }
  }

  function validateForm() {
    let valid = true;
    const errorsCopy = { ...errors };

    if (!username.trim()) {
      errorsCopy.username = "Username required";
      valid = false;
    } else {
      errorsCopy.username = "";
    }

    if (!email.trim()) {
      errorsCopy.email = "Email required";
      valid = false;
    } else if (!isValidEmail(email)) {
      errorsCopy.email = "Invalid email address";
      valid = false;
    } else {
      errorsCopy.email = "";
    }

    if (!password.trim()) {
      errorsCopy.password = "Password required";
      valid = false;
    } else if (!isValidPassword(password)) {
      errorsCopy.password = "Password must be at least 6 characters long";
      valid = false;
    } else {
      errorsCopy.password = "";
    }

    if (!phone_number.trim()) {
      errorsCopy.phone_number = "Phone number required";
      valid = false;
    } else if (!isValidPhoneNumber(phone_number)) {
      errorsCopy.phone_number = "Invalid phone number format";
      valid = false;
    } else {
      errorsCopy.phone_number = "";
    }

    setErrors(errorsCopy);
    return valid;
  }

  function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }

  function isValidPassword(password) {
    return password.length >= 6;
  }

  function isValidPhoneNumber(phone) {
    // Basic phone number validation - accepts formats like: 1234567890, 123-456-7890, (123) 456-7890
    const phoneRegex = /^[\+]?[1-9][\d]{0,15}$|^[\+]?[(]?[\d\s\-\(\)]{10,}$/;
    return phoneRegex.test(phone.replace(/[\s\-\(\)]/g, ''));
  }

  return (
    <div className="signup-page">
      <div className="signup-container">
        <div className="signup-image-section">
          <img
            src={loginImage}
            alt="Signup Page"
            className="signup-image"
          />
        </div>
        <div className="signup-form-section">
          <div className="signup-form-container">
            <h2 className="signup-title">Create Account</h2>
            {registerMessage && (
              <div className={`register-message ${registerMessage.includes("successfully") ? "success" : "error"}`}>
                {registerMessage}
              </div>
            )}
            <form onSubmit={handleRegistrationForm}>
              <div className="form-group">
                <input
                  type="text"
                  name="username"
                  className={`form-input ${errors.username ? "input-error" : ""}`}
                  placeholder="Username"
                  value={username}
                  onChange={(event) => setUsername(event.target.value)}
                />
                {errors.username && (
                  <div className="error-message">{errors.username}</div>
                )}
              </div>
              <div className="form-group">
                <input
                  type="text"
                  name="email"
                  className={`form-input ${errors.email ? "input-error" : ""}`}
                  placeholder="Email"
                  value={email}
                  onChange={(event) => setEmail(event.target.value)}
                />
                {errors.email && (
                  <div className="error-message">{errors.email}</div>
                )}
              </div>
              <div className="form-group">
                <input
                  type="password"
                  name="password"
                  className={`form-input ${errors.password ? "input-error" : ""}`}
                  placeholder="Password"
                  value={password}
                  onChange={(event) => setPassword(event.target.value)}
                />
                {errors.password && (
                  <div className="error-message">{errors.password}</div>
                )}
              </div>
              <div className="form-group">
                <input
                  type="text"
                  name="phone_number"
                  className={`form-input ${errors.phone_number ? "input-error" : ""}`}
                  placeholder="Phone Number"
                  value={phone_number}
                  onChange={(event) => setPhonenumber(event.target.value)}
                />
                {errors.phone_number && (
                  <div className="error-message">{errors.phone_number}</div>
                )}
              </div>
              <div className="form-actions">
                <button type="submit" className="signup-button">
                  Create Account
                </button>
              </div>
            </form>
            <div className="signup-links">
              {/*<p>Already have an account? <a href="/login">Login</a></p>*/}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CreateAccount;
