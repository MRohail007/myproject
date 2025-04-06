import React from 'react';
import './styles/Sidebar.css'; // Assuming you will create a separate CSS file for styles

const Sidebar = () => {
  return (
    <div className="sidebar">
      <a href="Physiotherapistdashboard.html">
        <div className="dashboard">
          <img src="dashboardicon.svg" className="icon" alt=" " /> Dashboard
        </div>
      </a>
      <a href="physiotherapistnotification.html">
        <div className="notification">
          <img src="notificationicon.svg" className="icon" alt=" " />
          Notifications
        </div>
      </a>
      <a href="#">
        <div className="patients">
          <img src="patientsicon.png" className="icon" alt=" " />
          Patients
        </div>
      </a>
      {/* <p className="physiotherapistpatients">Patients</p> */}
    </div>
  );
};

const PatientCard = ({ name, dob, email, phone, nextAppointment }) => {
  return (
    <div className="patient">
      <div className="top">
        <h3 className="h3">{name}</h3>
        <p className="dob">DOB: {dob}</p>
      </div>
      <div className="bottom">
        <p className="fonts">Email: {email}</p>
        <p className="fonts">Phone: {phone}</p>
        <p className="fonts">Next Appointment: {nextAppointment}</p>
      </div>
    </div>
  );
};

const PatientsList = () => {
  const patients = [
    {
      name: 'Alice Johnson',
      dob: '1985-03-15',
      email: 'alice.johnson@example.com',
      phone: '+1 (555) 123-4567',
      nextAppointment: '2023-06-20 10:00 AM',
    },
    {
      name: 'David Luiz',
      dob: '1985-03-15',
      email: 'davidluiz123.com',
      phone: '+1 (555) 123-4567',
      nextAppointment: '2023-06-20 10:00 AM',
    },
    {
      name: 'Cristiano Ronaldo',
      dob: '1985-03-15',
      email: 'cristiano.ronaldo@example.com',
      phone: '+1 (555) 123-4567',
      nextAppointment: '2023-06-20 10:00 AM',
    },
  ];

  return (
    <> </>
    // <div className="Patients">
    //     {patients.map((patient, index) => (
    //         <PatientCard key={index} {...patient} />
    //     ))}
    // </div>
  );
};

export default Sidebar;
