import React, { useState, useEffect } from "react";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "../css/tasks.css";
import axios from "axios";
import { useNavigate, useLocation } from "react-router-dom";

const UpdateTask = () => {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [dueDate, setDueDate] = useState("");
  const [notifyBeforeHours, setNotifyBeforeHours] = useState(24);
  const [priority, setPriority] = useState("Medium");
  const navigate = useNavigate();
  const location = useLocation();

  // Get the reminder data passed from TaskComponent
  const reminder = location.state;

  // Pre-fill form with existing data
  useEffect(() => {
    if (reminder) {
      setTitle(reminder.title || "");
      setDescription(reminder.description || "");
      
      // Format date for datetime-local input
      const dueDateObj = new Date(reminder.dueDate);
      const formattedDate = dueDateObj.toISOString().slice(0, 16);
      setDueDate(formattedDate);
      
      setNotifyBeforeHours(reminder.notifyBeforeHours || 24);
      setPriority(reminder.priority || "Medium");
    }
  }, [reminder]);

  const handleSubmit = async (e) => {
  e.preventDefault();

  if (!title.trim() || !dueDate) {
    toast.error("Please fill required fields");
    return;
  }

  const updatedReminder = {
    title,
    description,
    dueDate,
    notifyBeforeHours,
    priority,
    notified: reminder.notified || false,
    uiNotified: reminder.uiNotified || false,
  };

  try {
     const token= sessionStorage.getItem("jwtToken");
    console.log("Sending PUT request...");
    console.log("Reminder ID:", reminder.id);
    console.log("Data being sent:", updatedReminder);
    
    const response = await axios.put(
      `http://localhost:8080/reminders/${reminder.id}`,
      updatedReminder,
      { headers: {
      Authorization: `Bearer ${token}`}}
    );
    
   
    toast.success("Task updated successfully!");
    setTimeout(() => navigate("/tasks/all"), 1000);
  } catch (err) {
    
    console.error("Error status:", err.response?.status);
    
    toast.error(`Error updating task: ${err.response?.data?.message || err.message}`);
  }
};

  const handleCancel = () => {
    navigate("/tasks/all");
  };

  if (!reminder) {
    return <div>Loading...</div>;
  }

  return (
    <div className="add-reminder-container">
      <h2>Update Reminder</h2>
      <form className="add-reminder-form" onSubmit={handleSubmit}>
        <label>
          Title<span className="required">*</span>
          <input 
            type="text" 
            value={title} 
            onChange={(e) => setTitle(e.target.value)} 
            required 
          />
        </label>

        <label>
          Description
          <textarea 
            rows="3" 
            value={description} 
            onChange={(e) => setDescription(e.target.value)} 
          />
        </label>

        <label>
          Due Date & Time<span className="required">*</span>
          <input 
            type="datetime-local" 
            value={dueDate} 
            onChange={(e) => setDueDate(e.target.value)} 
            required 
          />
        </label>

        <label>
          Notify Before
          <select 
            value={notifyBeforeHours} 
            onChange={(e) => setNotifyBeforeHours(parseInt(e.target.value))}
          >
            <option value={24}>24 hours</option>
            <option value={48}>48 hours</option>
          </select>
        </label>

        <label>
          Priority
          <select 
            value={priority} 
            onChange={(e) => setPriority(e.target.value)}
          >
            <option value="High">High</option>
            <option value="Medium">Medium</option>
            <option value="Low">Low</option>
          </select>
        </label>

        <div className="form-buttons">
          <button type="button" onClick={handleCancel} className="cancel-btn">
            Cancel
          </button>
          <button type="submit" className="update-btn">
            Update Reminder
          </button>
        </div>
      </form>

      <ToastContainer position="top-right" autoClose={5000} />
    </div>
  );
};

export default UpdateTask;