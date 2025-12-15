import React from "react";
import "../css/notification.css";

const EnhancedNotification = ({ title, message, onClose }) => {
  return (
    <div className="notif-overlay">
      <div className="notif-card">
        <button className="notif-close" onClick={onClose}>×</button>

        <h3 className="notif-title">{title}</h3>
        <p className="notif-message">{message}</p>

        <button className="notif-ok" onClick={onClose}>OK</button>
      </div>
    </div>
  );
};

export default EnhancedNotification;
