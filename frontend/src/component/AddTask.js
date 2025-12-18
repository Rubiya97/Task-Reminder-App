import React, { useState } from "react";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "../css/tasks.css";
import axios from "axios";
import { useNavigate } from "react-router-dom";
//add task
const AddTask = ({ checkNotifications }) => {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [dueDate, setDueDate] = useState("");
  const [notifyBeforeHours, setNotifyBeforeHours] = useState(24); // default 24
  const [priority, setPriority] = useState("Medium"); // default medium
  const navigate = useNavigate();

 const handleSubmit = async (e) => {
  e.preventDefault();

  if (!title.trim() || !dueDate) {
    toast.error("Please fill required fields");
    return;
  }

  const reminder = {
    title,
    description,
    dueDate,
    notifyBeforeHours,
    priority,
    notified: false,
    uiNotified: false,
  };
  const token= sessionStorage.getItem("jwtToken");

  try {
    console.log("TOKEN:", sessionStorage.getItem("jwtToken"));
    // Save reminder first
    const response = await axios.post("http://localhost:8080/reminders/add", reminder, {headers: {
      Authorization: `Bearer ${token}`}});
    const savedReminder = response.data;
    
    toast.success("Reminder added");

    
    
    console.log(`New reminder created: ${title}`);
    console.log(`Due: ${new Date(dueDate)}`);
    console.log(`Notify before: ${notifyBeforeHours} hours`);

    // Trigger global check - the main hook will handle exact timing
    if (checkNotifications) {
      setTimeout(() => checkNotifications(), 1000);
    }

    setTimeout(() => navigate("/tasks/all"), 3000);

    // Clear form
    setTitle("");
    setDescription("");
    setDueDate("");
    setNotifyBeforeHours(24);
    setPriority("Medium");
  } catch (err) {
    console.error(err);
    toast.error("Error adding reminder.");
  }

  };

  return (
    <div className="add-reminder-container">
      <h2>Add Reminder</h2>
      <form className="add-reminder-form" onSubmit={handleSubmit}>
        <label>
          Title<span className="required">*</span>
          <input type="text" value={title} onChange={(e) => setTitle(e.target.value)} required />
        </label>

        <label>
          Description
          <textarea rows="3" value={description} onChange={(e) => setDescription(e.target.value)} />
        </label>

        <label>
          Due Date & Time<span className="required">*</span>
          <input type="datetime-local" value={dueDate} onChange={(e) => setDueDate(e.target.value)} required />
        </label>

        <label>
          Notify Before
          <select value={notifyBeforeHours} onChange={(e) => setNotifyBeforeHours(parseInt(e.target.value))}>
            <option value={24}>24 hours</option>
            <option value={48}>48 hours</option>
          </select>
        </label>

        <label>
          Priority
          <select value={priority} onChange={(e) => setPriority(e.target.value)}>
            <option value="High">High</option>
            <option value="Medium">Medium</option>
            <option value="Low">Low</option>
          </select>
        </label>

        <button type="submit">Add Reminder</button>
      </form>

      <ToastContainer position="top-right" autoClose={5000} />
    </div>
  );
};

export default AddTask;
