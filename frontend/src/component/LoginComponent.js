import React, { useState } from "react";
import { loginApi, saveLoggedUser } from "../service/AuthApiService";
import { useNavigate } from "react-router-dom";
import "../css/tasks.css";
import loginImage from "../assets/login.jpg";

const LoginComponent = ({ setIsAuth }) => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
 
  const [loginMessage, setLoginMessage] = useState("");

  // for input validation
  const [errors, setErrors] = useState({});

  const navigate = useNavigate();

  // validate fields
  const validateForm = () => {
    const tempErrors = {};
    if (!username.trim()) tempErrors.username = "Username is required";
    if (!password.trim()) tempErrors.password = "Password is required";
    setErrors(tempErrors);
    return Object.keys(tempErrors).length === 0;
  };

 

const handleLogin = async (e) => {
  e.preventDefault();

  if (!validateForm()) return;
  try {
    const result = await loginApi(username, password); // call your API

    // The backend now wraps the data in ApiResponse
    if (result.success && result.data) {
      const { token, user_id, username, email } = result.data;

      // Save JWT in sessionStorage or localStorage
      sessionStorage.setItem("jwtToken", token);

      // Save user info in state/context
      saveLoggedUser({
        username,
        user_id,
        email,
      });

      setIsAuth(true);
      setLoginMessage("Login successful!");
      navigate("/tasks/all");
    } else {
      // backend returns success=false for invalid login
      setLoginMessage(result.message || "Incorrect username or password");
    }
  } catch (error) {
    console.error(error);
    setLoginMessage("Login failed. Please try again.");
  }
};



  return (
    <div className="login-page">
      <div className="login-container">

        {/* LEFT IMAGE SECTION */}
        <div className="login-image-section">
          <img src={loginImage} alt="Login Page" className="login-image" />
        </div>

        {/* RIGHT FORM SECTION */}
        <div className="login-form-section">
          <div className="login-form-container">
            <h2 className="login-title">Login</h2>

            {loginMessage && (
              <div
                className={`login-message ${
                  loginMessage.includes("successful") ? "success" : "error"
                }`}
              >
                {loginMessage}
              </div>
            )}

            <form onSubmit={handleLogin}>
              {/* USERNAME */}
              <div className="form-group">
                <input
                  type="text"
                  name="username"
                  className={`form-input ${errors.username ? "input-error" : ""}`}
                  placeholder="Username"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                />
                {errors.username && (
                  <div className="error-message">{errors.username}</div>
                )}
              </div>

              {/* PASSWORD */}
              <div className="form-group">
                <input
                  type="password"
                  name="password"
                  className={`form-input ${errors.password ? "input-error" : ""}`}
                  placeholder="Password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
                {errors.password && (
                  <div className="error-message">{errors.password}</div>
                )}
              </div>

              {/* SUBMIT BUTTON */}
              <div className="form-actions">
                <button type="submit" className="login-button">
                  Login
                </button>
              </div>
            </form>

            <div className="login-links">
              {/* <p>Don't have an account? <a href="/register">Sign up</a></p> */}
            </div>
          </div>
        </div>

      </div>
    </div>
  );
};

export default LoginComponent;
