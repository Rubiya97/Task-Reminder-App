import axios from "axios";
import React, { useState } from "react";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";

import HeaderComponent from "./component/HeaderComponent";
import CreateAccount from "./component/CreateAccount";
import LoginComponent from "./component/LoginComponent";
import AddTask from "./component/AddTask";
import TaskComponent from "./component/TaskComponent";
import Home from "./component/Home";
import { isUserLoggedIn } from "./service/AuthApiService";
import ProfileComponent from "./component/profile";
import useTaskNotifications from "./hooks/TaskNotification";
import UpdateTask from "./component/UpdateTask";
import ViewTask from "./component/ViewTask";
axios.defaults.withCredentials = true;

const AuthenticatedRoute = ({ children, isLoggedIn }) => (isLoggedIn ? children : <Navigate to="/login" />);

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(isUserLoggedIn());
  const [notif, setNotif] = useState(null);

  // Only ONE hook instance
  const checkNotifications = useTaskNotifications(isLoggedIn,setNotif);
  
  return (
    <BrowserRouter>
      <HeaderComponent isAuth={isLoggedIn} setIsAuth={setIsLoggedIn} />
      
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/create-account" element={<CreateAccount />} />
        <Route path="/login" element={<LoginComponent setIsAuth={setIsLoggedIn} />} />
        <Route
          path="/tasks"
          element={
            <AuthenticatedRoute isLoggedIn={isLoggedIn}>
              <AddTask checkNotifications={checkNotifications} />
            </AuthenticatedRoute>
          }
        />
        <Route path="/tasks/all"
  element={<TaskComponent notif={notif} setNotif={setNotif} />}
/>

        <Route
          path="/profile"
          element={
            <AuthenticatedRoute isLoggedIn={isLoggedIn}>
              <ProfileComponent />
            </AuthenticatedRoute>
          }
        />
        // In App.js
<Route 
  path="/update-task" 
  element={
    <AuthenticatedRoute isLoggedIn={isLoggedIn}>
      <UpdateTask /> {/* or UpdateReminder */}
    </AuthenticatedRoute>
  } 
/>

<Route 
  path="/view-task" 
  element={
    <AuthenticatedRoute isLoggedIn={isLoggedIn}>
      <ViewTask /> {/* or UpdateReminder */}
    </AuthenticatedRoute>
  } 
/>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
