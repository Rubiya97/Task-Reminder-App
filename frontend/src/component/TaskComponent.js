import React, { useEffect, useState } from "react";
import { getLoggedUser } from "../service/AuthApiService";
import axios from "axios";
import { FaPlus, FaTrash, FaPen, FaEye } from "react-icons/fa";
import { toast, ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import "../css/tasks.css";
import { useNavigate } from "react-router-dom";
import ConfirmationModal from "./ConfirmationModal";
import EnhancedNotification from "./EnhancedNotification";




const TaskComponent = ({notif,setNotif}) => {
  const [reminders, setReminders] = useState([]);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [reminderToDelete, setReminderToDelete] = useState(null);
  //const [notif, setNotif] = useState(null);

  const navigate = useNavigate();
  const user = getLoggedUser();

  const fetchReminders = async () => {
    const token= sessionStorage.getItem("jwtToken");
    console.log("TOKEN:", sessionStorage.getItem("jwtToken"));
    try {
      const response = await axios.get("http://localhost:8080/reminders/mine", { headers: {
      Authorization: `Bearer ${token}`}});
      setReminders(response.data || []);
    } catch (err) {
      console.error(err);
      toast.error("Failed to fetch reminders.");
    }
  };

  useEffect(() => {
    fetchReminders();
  }, []);

  // Open delete confirmation modal
  const openDeleteModal = (reminder) => {
    setReminderToDelete(reminder);
    setShowDeleteModal(true);
  };

  // Close modal without deleting
  const closeDeleteModal = () => {
    setShowDeleteModal(false);
    setReminderToDelete(null);
  };

  // Delete function
  const deleteReminder = async (id) => {
    try {
      console.log("🗑️ Attempting to delete reminder ID:", id);
       const token= sessionStorage.getItem("jwtToken");
      const response = await axios.delete(`http://localhost:8080/reminders/${id}`, { 
        headers: {
      Authorization: `Bearer ${token}`} 
      });
      
      console.log("Delete successful:", response.data);
      toast.success("Reminder deleted successfully");
      fetchReminders();
    } catch (err) {
      console.error("Delete error details:");
      console.error("Error:", err);
      console.error("Error response:", err.response);
      console.error("Error status:", err.response?.status);
      console.error("Error data:", err.response?.data);
      console.error("Error message:", err.message);
      
      toast.error(`Failed to delete reminder: ${err.response?.data?.message || err.message}`);
    } finally {
      closeDeleteModal();
    }
  };

  // Confirm and delete the reminder
  const confirmDelete = async () => {
    if (!reminderToDelete) return;
    await deleteReminder(reminderToDelete.id);
  };

  // In TaskComponent.js - change this line:
const updateReminder = (reminder) => {
  navigate("/update-task", { state: reminder }); // ← Change to /update-task
};

  const viewReminder = (reminder) => {
    navigate("/view-task", { state: reminder });
  };

  return (
    <div className="reminder-page">
        
      <div className="reminder-header">
        <h2>My Reminders</h2>
        <button className="add-button" onClick={() => navigate("/tasks")}>
          <FaPlus /> Add Reminder
        </button>
      </div>

      <div className="reminder-list">
        {reminders.length === 0 && <p>No reminders yet.</p>}
        {reminders.map((reminder) => (
          <div key={reminder.id} className="reminder-card">
            <div className="reminder-actions">
              <button onClick={() => viewReminder(reminder)}><FaEye /></button>
              <button onClick={() => updateReminder(reminder)}><FaPen /></button>
              <button onClick={() => openDeleteModal(reminder)}><FaTrash /></button>

            </div>
            <h3 className="reminder-title">{reminder.title}</h3>
            <p className="reminder-desc">{reminder.description}</p>
            <div className="reminder-meta">
              <span
                 style={{
                       color:
                        reminder.priority === "High"
                            ? "red"
                            : reminder.priority === "Medium"
                             ? "orange"
                             : "green",
                     }}
                  >
                          Priority: {reminder.priority}
            </span>
              <span style={{ color: "red" }}>
  Due: {new Date(reminder.dueDate).toLocaleString()}
</span>

            </div>
          </div>
        ))}
      </div>

      <ToastContainer position="top-right" autoClose={5000} />

        {showDeleteModal && (
  <ConfirmationModal
    message="Are you sure you want to delete this reminder?"
    onConfirm={confirmDelete}
    onCancel={closeDeleteModal}
  />
)}
{notif && (
  <EnhancedNotification
    title={notif.title}
    message={notif.message}
    onClose={() => setNotif(null)}
  />
)}


    
    </div>
  );
};

export default TaskComponent;
