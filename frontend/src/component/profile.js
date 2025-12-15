import React, { useEffect, useState } from "react";
import { getLoggedUser } from "../service/AuthApiService";
import axios from "axios";
import "../css/profile.css";
import { useNavigate } from "react-router-dom";
import { FaPlus } from "react-icons/fa";
import { toast, ToastContainer } from "react-toastify";

const ProfileComponent = () => {
  const [profile, setProfile] = useState({});
  const navigate = useNavigate();

  const myprofile = async () => {
    try {
        const token= sessionStorage.getItem("jwtToken");
      const response = await axios.get(
        "http://localhost:8080/user/profile",
        {headers: {
      Authorization: `Bearer ${token}`} }
      );
      setProfile(response.data || {});
    } catch (err) {
      console.error(err);
      toast.error("Failed to get profile");
    }
  };

  useEffect(() => {
    myprofile();
  }, []);

  return (
    <div className="profile-container">
      <h2>My Profile</h2>

      <p><strong>Username:</strong> {profile.username}</p>
      <p><strong>Email:</strong> {profile.email}</p>
      <p><strong>Phone Number:</strong> {profile.phoneNumber}</p>

      <button 
        className="home-button"
        onClick={() => navigate("/tasks/all")}
      >
         Home
      </button>
    </div>
  );
};

export default ProfileComponent;

