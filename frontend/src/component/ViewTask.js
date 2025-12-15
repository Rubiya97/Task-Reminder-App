import React, { useEffect, useState } from "react";
import { useNavigate, useParams, useLocation } from "react-router-dom";
import axios from "axios";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "../css/tasks.css";

const ViewTask = () => {
  const [reminder, setReminder] = useState(null);
  const navigate = useNavigate();
  const { id } = useParams();
  const location = useLocation();
  const stateReminder = location.state;

  useEffect(() => {
    if (stateReminder) {
      setReminder(stateReminder);
    } else {
      const fetchReminder = async () => {
        try {
          const token = sessionStorage.getItem("jwtToken");
          const response = await axios.get(`http://localhost:8080/reminders/${id}`, {
            headers: { Authorization: `Bearer ${token}` },
          });
          setReminder(response.data);
        } catch (error) {
          console.error(error);
          toast.error("Error fetching reminder");
          navigate("/tasks/all");
        }
      };
      fetchReminder();
    }
  }, [id, stateReminder, navigate]);

  const handleHome = () => navigate("/tasks/all");

  if (!reminder) return <div>Loading...</div>;

  // Format date for datetime-local display style
  const formattedDate = new Date(reminder.dueDate).toISOString().slice(0, 16);

  return (
    <div className="add-reminder-container">
      <h2>View Reminder</h2>
      <form className="add-reminder-form">
        <label>
          Title
          <input type="text" value={reminder.title} readOnly />
        </label>

        <label>
          Description
          <textarea rows="3" value={reminder.description || ""} readOnly />
        </label>

        <label>
          Due Date & Time
          <input type="datetime-local" value={formattedDate} readOnly />
        </label>

        <label>
          Notify Before
          <input type="text" value={`${reminder.notifyBeforeHours} hours`} readOnly />
        </label>

        <label>
          Priority
          <input type="text" value={reminder.priority} readOnly />
        </label>

        

        <div className="form-buttons">
          <button type="button" onClick={handleHome} className="cancel-btn">
            Home
          </button>
        </div>
      </form>
      <ToastContainer position="top-right" autoClose={5000} />
    </div>
  );
};

export default ViewTask;
