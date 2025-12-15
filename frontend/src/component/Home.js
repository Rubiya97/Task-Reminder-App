import React from 'react';
import '../css/HomePage.css';
import loginImage from "../assets/login.jpg"; // Import the image

const HomePage = () => {
    return (
      <div className="center-in-page">
        <div className="home-container">
          <div className="home-content">
            <h1>Welcome to <span className="text-primary">Task Reminder </span>Web Application</h1>
            <p className="lead">
              Experience a smarter way to manage tasks. Boost productivity, collaborate seamlessly, and accomplish more in less time.
            </p>
          </div>
        </div>
      </div>
    );
  };

export default HomePage;