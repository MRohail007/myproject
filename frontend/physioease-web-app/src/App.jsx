import './App.css'
import React, { useState } from 'react'
import { BrowserRouter as Router, Route, Routes, useNavigate, Link } from 'react-router-dom'
import PatientDashboard from './components/patient/PatientDashboard'
import HomePage from './components/HomePage'
import { usePatient } from './context/PatientContext'
import Chat from './components/chat/Chat'
import ScheduleAppointment from './components/ScheduleAppointment'

function App() {
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(true);
  const [isProfileModalOpen, setIsProfileModalOpen] = useState(false);
  const { patientId } = usePatient();
  const userRole = patientId ? 'patient' : null; // Check if user is a patient

  const toggleDropdown = () => {
    setDropdownOpen(!dropdownOpen);
  };

  return (
    <Router>
      <header className="text-gray-600 body-font pb-8">
        <div className="mx-auto flex flex-wrap flex-col md:flex-row items-center">
          <a className="flex title-font font-medium items-center text-gray-900 mb-4 md:mb-0">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" className="w-10 h-10 text-white p-2 bg-indigo-500 rounded-full" viewBox="0 0 24 24">
              <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5"></path>
            </svg>
            <span className="ml-3 text-xl">PhysioEase</span>
          </a>
          {/* Navbar */}
          <nav className="md:mr-auto md:ml-4 md:py-1 md:pl-4 md:border-l md:border-gray-400 flex flex-wrap items-center text-base justify-center">
            <Link className="mr-5 hover:text-gray-900" to="/home-page"> Home</Link>
            <Link className="mr-5 hover:text-gray-900" to="/chat"> Chat</Link>
            <Link className="mr-5 hover:text-gray-900" to="/book"> Sheduler</Link>

            <a className="mr-5 hover:text-gray-900">Contact</a>
            <a className="mr-5 hover:text-gray-900">About</a>
            {/* <button className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-lg flex items-center">
              <Search className="w-5 h-5 mr-2" /> Find a Physiotherapist
            </button> */}
          </nav>
          <div className="flex items-center space-x-4">
            <Link to="/login" className=" px-2 py-2 border-blue-500 rounded hover:bg-transparent transition">
            Login
            </Link>
            <Link to="/register" className= "px-7 py-2 border-blue-500 rounded hover:bg-transparent transition">
            Register
            </Link>
          </div>
          {/* Auth Buttons */}
          {isLoggedIn && (
            <div className="relative">
              <button
                onClick={toggleDropdown}
                className="inline-flex items-center bg-blue-500 border-0 p-2 focus:outline-none hover:bg-blue-600 rounded text-base mt-4 md:mt-0"
              >
                {dropdownOpen ? ( // If menu is open, show "X" icon
                  <svg fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" className="w-6 h-6 stroke-white" viewBox="0 0 24 24">
                    <path d="M6 18L18 6M6 6l12 12"></path>
                  </svg>
                ) : (
                  <svg fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" className="w-6 h-6 stroke-white" viewBox="0 0 24 24">
                    <path d="M4 6h16M4 12h16m-7 6h7"></path>
                  </svg>
                )}
              </button>
              {dropdownOpen && <DropdownMenu setIsProfileModalOpen={setIsProfileModalOpen} userRole={userRole} />}
            </div>
          )}
        </div>
      </header>
      {/* Main Content */}
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/chat" element={<Chat />} />
        <Route path="/patient-dashboard" element={<PatientDashboard />} />
        <Route path="/home-page" element={<HomePage />} />
        <Route path="/book" element={<ScheduleAppointment />} />
      </Routes>
    </Router>
  )
}

// Todo: Move to a navbar component 
function DropdownMenu({ setIsProfileModalOpen, userRole }) {
  const navigate = useNavigate();
  if (!userRole) return null;

  return (
    <div className="absolute right-0 mt-2 w-48 bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700 rounded-lg shadow-lg">
      <a href="#" onClick={() => setIsProfileModalOpen(true)} className="block px-4 py-2 text-gray-800 dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700">Edit Profile</a>
      <a href="#" onClick={() => navigate('/patient-dashboard')} className="block px-4 py-2 text-gray-800 dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700">Dashboard</a>
      <a href="#" className="block px-4 py-2 text-gray-800 dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700">About</a>
    </div>
  );
}

export default App
