import React, { useEffect } from 'react';
import './styles/leftconer.css'; // Importing the left corner CSS
import './styles/middlecontent.css'
import './styles/rightcontent.css'
// import './rightcontent.css'; // Assuming you have this file
// import './middlecontent.css'; // Assuming you have this file
// import FullCalendar from '@fullcalendar/react';
// import dayGridPlugin from '@fullcalendar/daygrid';
// import './App.css'; // Assuming you have a main CSS file for global styles

const PhysiotherapistDashboard = () => {
  // useEffect(() => {
  //     // Initialize FullCalendar
  //     const calendarEl = document.getElementById('calendar');
  //     const calendar = new FullCalendar.Calendar(calendarEl, {
  //         initialView: 'dayGridMonth',
  //         headerToolbar: {
  //             left: 'prev,next today',
  //             center: 'title',
  //             right: 'dayGridMonth,dayGridWeek,dayGridDay'
  //         },
  //         selectable: true,
  //         dateClick: function(info) {
  //             alert('You clicked on ' + info.dateStr); // Action when a date is clicked
  //         }
  //     });
  //     calendar.render();
  // }, []);

  return (
    <div>
      <div className="sidebar">
        <div className="dashboard">
          <img src="dashboardicon.svg" className="icon" alt=" " /> Dashboard
        </div>
        <a href="physiotherapistnotification.html">
          <div className="notification">
            <img src="notificationicon.svg" className="icon" alt=" " /> Notifications
          </div>
        </a>
        <a href="physiotherapistpatients.html">
          <div className="patients">
            <img src="patientsicon.png" className="icon" alt=" " /> Patients
          </div>
        </a>
      </div>

      <p className="physiotherapistdashboard">Physiotherapist Dashboard</p>
      <div className="middlecontent">
        <p className="schedule">Schedule</p>
        <div id="calendar" className="calenderrender"></div>
      </div>

      <div className="rightcontent">
        <p className="rightcontentheader">Set Availability</p>
        <label htmlFor="start-time">Start Time</label>
        <input type="time" id="start-time" className="input-box" />

        <label htmlFor="end-time">End Time</label>
        <input type="time" id="end-time" className="input-box" />

        <div className="dropdown">
          <label htmlFor="patients">Repeat</label>
          <img src="arrow.png" className="arrowicon" alt="Arrow Icon" />
          <select id="patients" className="input-box">
            <option value="patient1">Daily</option>
            <option value="patient2">Weekly</option>
            <option value="patient3">Monthly</option>
          </select>
        </div>
        <div className="setavailability">Set Availability</div>
      </div>

      <div className="rightbottomcontent">
        <p className="rightcontentheader">Upcoming Appointments</p>
        <div className="appointmentinfo">
          <div className="patientname">John Smith</div>
          <div className="appointmenttime">09:00 AM</div>
          <div className="appointmentdate">2023-06-15</div>
        </div>

        <div className="appointmentinfo">
          <div className="patientname">David Luiz</div>
          <div className="appointmenttime">12:00 PM</div>
          <div className="appointmentdate">2024-06-15</div>
        </div>

        <div className="appointmentinfo">
          <div className="patientname">Cristiano Ronaldo</div>
          <div className="appointmenttime">03:00 PM</div>
          <div className="appointmentdate">2025-07-15</div>
        </div>
      </div>
    </div>
  );
};

export default PhysiotherapistDashboard;
