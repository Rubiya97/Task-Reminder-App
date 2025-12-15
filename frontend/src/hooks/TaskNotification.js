import { useEffect, useRef, useCallback } from "react";
import axios from "axios";


const useTaskNotifications = (isLoggedIn,setNotif) => {
  const notifiedSet = useRef(new Set());
  

  const checkNotifications = useCallback(async () => {
    if (!isLoggedIn) return;

    try {
      const token= sessionStorage.getItem("jwtToken");
      console.log("Checking UI notifications...");
      const res = await axios.get("http://localhost:8080/reminders/mine", { 
       headers: {
      Authorization: `Bearer ${token}`}
      });
      const reminders = res.data || [];
      const now = new Date();

      console.log(`Found ${reminders.length} reminders`);
      console.log(` Current time: ${now}`);

      for (const r of reminders) {
        // Skip if already notified
        if (r.uiNotified || notifiedSet.current.has(r.id)) {
          console.log(`Skipping - Already notified: ${r.title}`);
          continue;
        }

        if (!r.dueDate) continue;

        const dueDate = new Date(r.dueDate); 
        const currentTime = new Date();
        const notifyBeforeHours = r.notifyBeforeHours; 

        const target24 = 24 * 60 * 60 * 1000;
        const target48 = 48 * 60 * 60 * 1000;

        // Calculate time difference
        const timeUntilDue = dueDate - currentTime;
        
        // Determine which target to use based on notifyBeforeHours
        const targetMs = notifyBeforeHours === 48 ? target48 : target24;
        
        // Check if we're within 1 minute of the target time
        if (Math.abs(timeUntilDue - targetMs) < 30000) {
          let message = `${notifyBeforeHours} hr`;
          
          console.log(`SENDING EXACT NOTIFICATION: ${r.title} - ${message}`);
          
          /*new Notification(`Reminder: ${r.title}`, {
            body: `${message} - ${r.description || 'No description'}`,
            icon: "/favicon.ico",
          });*/
          setNotif({
  title: `Reminder: ${r.title}`,
  message: `${message} - ${r.description || "No description"}`
});


          // Mark as notified
          notifiedSet.current.add(r.id);
          try {
            const token= sessionStorage.getItem("jwtToken");
            await axios.patch(`http://localhost:8080/reminders/ui-notified/${r.id}`, {}, { headers: {
      Authorization: `Bearer ${token}`}
            });
            console.log("Marked as UI notified in backend");
          } catch (err) {
            console.log("Could not mark as UI notified in backend");
          }
        } else if(dueDate < currentTime || timeUntilDue < target24){   //OVERDUE while creating the task itself then this will execute

             let message = `${notifyBeforeHours} hr`;

           /* new Notification(`Reminder: ${r.title}`, {
            body: `${message} - ${r.description || 'No description'}`,
            icon: "/favicon.ico",
          });*/
          setNotif({
  title: `Reminder: ${r.title}`,
  message: `${message} - ${r.description || "No description"}`
});

          // Mark as notified
          notifiedSet.current.add(r.id);
          try {
            const token= sessionStorage.getItem("jwtToken");
            await axios.patch(`http://localhost:8080/reminders/ui-notified/${r.id}`, {}, {headers: {
      Authorization: `Bearer ${token}`} 
            });
            console.log("✅ Marked as UI notified in backend");
          } catch (err) {
            console.log("⚠️ Could not mark as UI notified in backend");
          }
        }
        
        else {
          const hoursUntilTarget = (timeUntilDue - targetMs) / (1000 * 60 * 60);
          console.log(`⏳ Waiting to notify "${r.title}" in ${hoursUntilTarget.toFixed(2)} hours`);
        }
      }
    } catch (err) {
      console.error("UI Notification error:", err);
    }
  }, [isLoggedIn, setNotif]);

  useEffect(() => {
    if (!("Notification" in window)) {
      console.log("Notifications not supported");
      return;
    }

    console.log("UI Notification system starting...");
    
    if (Notification.permission === "default") {
      Notification.requestPermission().then(permission => {
        console.log(`Notification permission: ${permission}`);
      });
    }

    // Initial check
    checkNotifications();
    
    // Check every 30 seconds for more precise timing
    const interval = setInterval(checkNotifications, 30000);
    
    return () => {
      console.log("UI Notification system stopping...");
      clearInterval(interval);
    };
  }, [checkNotifications]);

  return checkNotifications;
};

export default useTaskNotifications;