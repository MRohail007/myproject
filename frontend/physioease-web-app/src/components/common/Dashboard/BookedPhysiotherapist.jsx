import React from 'react';
import { User, Phone, Mail, Calendar, Clock } from 'lucide-react';

const BookedPhysiotherapistInfo = () => {

  /*
  ======
   * TODO: Data has to be coming from the backend
  ======
   */
  const physiotherapist = {
    name: "Dr. Sarah Johnson",
    specialization: "Orthopedic",
    email: "sarah.johnson@physioease.com",
    phone: "+1 (555) 123-4567",
    experience: "10 years",
  };

  /*
  ======
  Todo: Patient needs to be able to cancel the appointment
  ======
   */
  const appointments = [
    {
      id: 1,
      date: "March 28, 2024",
      time: "10:00 AM",
      type: "Follow-up Session",
      status: "Confirmed"
    },
    {
      id: 2,
      date: "April 4, 2024",
      time: "2:30 PM",
      type: "Progress Assessment",
      status: "Pending"
    }
  ];

  
  return (
    <div className="bg-white p-4 rounded-lg shadow-sm w-2/4">
      <h2 className='text-lg font-medium mb-4'>Booked Physiotherapist</h2>
      <div className='flex items-center'>
        <User className='w-5 h-5 mr-2 text-gray-500' />
        <span className='text-gray-600 my-1'>{physiotherapist.name}</span>
      </div>
      <div className='flex items-center'>
        <Mail className='w-5 h-5 mr-2 text-gray-500'/>
        <span className='text-sm my-4'>{physiotherapist.email}</span>
      </div>
      <div className='flex items-center'>
        <Phone className='w-5 h-5 mr-2 text-gray-500'/>
        <span className='text-sm '>{physiotherapist.phone}</span>
      </div>

      <h2 className="text-lg font-medium mb-4 mt-6">Upcoming Appointments</h2>
      <div className="space-y-4">
        {appointments.map((appointment) => (
          <div key={appointment.id} className="border-l-4 border-blue-500 pl-4 py-2">
            <div className="flex items-center mb-1">
              <Calendar className="w-4 h-4 mr-2 text-gray-500" />
              <span className="font-medium">{appointment.date}</span>
            </div>
            <div className="flex items-center text-sm text-gray-600">
              <Clock className="w-4 h-4 mr-2" />
              <span>{appointment.time} - {appointment.type}</span>
            </div>
            <span className={`text-xs px-2 py-1 rounded-full ${
              appointment.status === 'Confirmed' 
                ? 'bg-green-100 text-green-800' 
                : 'bg-yellow-100 text-yellow-800'
            }`}>
              {appointment.status}
            </span>
          </div>
        ))}
      </div>
    </div>
  );
};

export default BookedPhysiotherapistInfo;
