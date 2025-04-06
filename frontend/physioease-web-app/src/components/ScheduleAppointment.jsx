import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './styles/ScheduleAppointment.css';

const ScheduleAppointment = () => {
  const [physiotherapists, setPhysiotherapists] = useState([]);
  const [selectedPhysio, setSelectedPhysio] = useState('');
  const [dateTime, setDateTime] = useState('');
  const [subject, setSubject] = useState('');
  const [body, setBody] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const user = { id: 2, username: 'Jane Smith', userType: 'Patient' };

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const physioResponse = await axios.get('http://localhost:8081/api/physiotherapists', {
          withCredentials: true,
        });
        setPhysiotherapists(physioResponse.data);
        setLoading(false);
      } catch (err) {
        console.error('Error fetching physiotherapists:', err.response?.data);
        const errorMessage = err.response?.data?.error || 'Failed to fetch physiotherapists. Please try again.';
        setError(errorMessage);
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
  
    if (!selectedPhysio || !dateTime || !subject || !body) {
      setError('Please fill in all required fields.');
      return;
    }
  
    const selectedDateTime = new Date(dateTime);
    const now = new Date();
    // Add a 2-minute buffer to the current time
    const minFutureTime = new Date(now.getTime() + 2 * 60 * 1000); // 2 minutes from now
  
    console.log('Selected DateTime (UTC):', selectedDateTime.toISOString());
    console.log('Current Time (UTC):', now.toISOString());
    console.log('Minimum Future Time (UTC):', minFutureTime.toISOString());
  
    if (selectedDateTime <= minFutureTime) {
      setError('Please select a date and time at least 2 minutes in the future.');
      return;
    }
  
    try {
      setLoading(true);
      const appointmentData = {
        physiotherapistId: selectedPhysio,
        patientId: user.id,
        dateTime: selectedDateTime.toISOString(),
        subject: subject,
        body: body,
      };
      console.log('Sending Appointment Data:', appointmentData);
  
      const response = await axios.post('http://localhost:8081/api/appointments', appointmentData, {
        withCredentials: true,
      });
  
      setSuccess(`Appointment Reminder successfully for ${selectedDateTime.toLocaleString()}`);
      setLoading(false);
      setSelectedPhysio('');
      setDateTime('');
      setSubject('');
      setBody('');
    } catch (err) {
      console.error('Error scheduling appointment:', err.response?.data);
      const errorMessage = err.response?.data || 'Failed to schedule appointment. Please try again.';
      setError(errorMessage);
      setLoading(false);
    }
  };
  return (
    <div className="schedule-container">
      <h2>Schedule an Appointment Reminder</h2>
      {error && <p className="error">{error}</p>}
      {success && <p className="success">{success}</p>}

      <form onSubmit={handleSubmit}>
        <div className="scform-group">
          <label htmlFor="physiotherapist">Select Physiotherapist:</label>
          <select
            id="physiotherapist"
            value={selectedPhysio}
            onChange={(e) => setSelectedPhysio(e.target.value)}
            disabled={loading}
          >
            <option value="">-- Select a Physiotherapist --</option>
            {physiotherapists.map((physio) => (
              <option key={physio.id} value={physio.id}>
                {physio.username} ({physio.specialty})
              </option>
            ))}
          </select>
        </div>

        <div className="scform-group">
          <label htmlFor="dateTime">Select Date and Time:</label>
          <input
            type="datetime-local"
            id="dateTime"
            value={dateTime}
            onChange={(e) => setDateTime(e.target.value)}
            disabled={loading || !selectedPhysio}
            min={new Date().toISOString().slice(0, 16)}
          />
        </div>

        <div className="scform-group">
          <label htmlFor="subject">Email Subject:</label>
          <input
            type="text"
            id="subject"
            value={subject}
            onChange={(e) => setSubject(e.target.value)}
            placeholder="Enter email subject"
            disabled={loading}
          />
        </div>

        <div className="scform-group">
          <label htmlFor="body">Email Body:</label>
          <textarea
            id="body"
            value={body}
            onChange={(e) => setBody(e.target.value)}
            placeholder="Enter email body (this will be sent to the physiotherapist)"
            rows="4"
            disabled={loading}
          />
        </div>

        <button className='scbutton' type="submit" disabled={loading}>
          {loading ? 'Scheduling...' : 'Schedule Appointment'}
        </button>
      </form>
    </div>
  );
};

export default ScheduleAppointment;