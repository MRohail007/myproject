import React, { useState, useEffect} from 'react';
import { Search, MapPin, Clock, Calendar, ArrowRight, Users, HeartPulse, Trophy } from 'lucide-react';
import physiotherapistApiServices from '@/services/physiotherapistApiServices';

const HomePage = () => {

  const [physiotherapists, setPhysiotherapists] = useState([]);

  useEffect(()=> {
    const fetchPhysiotherapists = async () => {
      try {
        const physiotherapistData = await physiotherapistApiServices.fetchPhysiotherapists();
        setPhysiotherapists(physiotherapistData);
      }catch(error) {
        console.error('Error fetching physiotherapists:', error);
      }
    };
    fetchPhysiotherapists();
  }, []);
  // const specialists = [
  //   { name: 'Sports Rehab Center', specialist: 'Dr. Sarah Chen', rooms: 4, image: '/api/placeholder/400/300' },
  //   { name: 'Spine Care Unit', specialist: 'Dr. James Wilson', rooms: 6, image: '/api/placeholder/400/300' },
  //   { name: 'Joint Recovery', specialist: 'Dr. Maya Patel', rooms: 3, image: '/api/placeholder/400/300' },
  //   { name: 'Physical Therapy Plus', specialist: 'Dr. Mark Thompson', rooms: 5, image: '/api/placeholder/400/300' },
  //   { name: 'Sports Rehab Center', specialist: 'Dr. Sarah Chen', rooms: 4, image: '/api/placeholder/400/300' },
  //   { name: 'Spine Care Unit', specialist: 'Dr. James Wilson', rooms: 6, image: '/api/placeholder/400/300' },
  //   { name: 'Joint Recovery', specialist: 'Dr. Maya Patel', rooms: 3, image: '/api/placeholder/400/300' },
  //   { name: 'Physical Therapy Plus', specialist: 'Dr. Mark Thompson', rooms: 5, image: '/api/placeholder/400/300' }
  // ];

  const statistics = [
    { icon: Users, value: '500+', label: 'Happy Patients' },
    { icon: HeartPulse, value: '95%', label: 'Success Rate' },
    { icon: Trophy, value: '10+', label: 'Awards Won' }
  ];

  return (
    <div className="min-h-screen bg-gray-100 ">


      <main className="w-full  mx-auto px-6 py-12">
        <div className="text-center mb-14">
          <h1 className="text-6xl font-bold mb-4">
            Your Perfect<br />
            <span className="text-blue-500 text-7xl">Healthcare Match</span><br />
            Awaits!
          </h1>
          <p className="text-gray-600 mx-auto mb-12 text-xl font-semibold">
            Skip the search hassle. Find your ideal physiotherapist with real-time availability and smart recommendations.
          </p>
        </div>

        <div className="flex gap-4 mb-12 max-w-3xl mx-auto">
          <button className="flex-1 bg-blue-500 hover:bg-blue-600 text-white p-4 rounded-xl flex items-center justify-center gap-2">
            <Search className="w-5 h-5" />
            <span>Available Appointments</span>
          </button>
          <button className="flex-1 bg-gray-200 hover:bg-gray-300 text-gray-800 p-4 rounded-xl flex items-center justify-center gap-2">
            <MapPin className="w-5 h-5" />
            <span>Explore Physiotherapists</span>
          </button>
        </div>

        {/* Statistics Section */}
        <section className="bg-white shadow-lg max-w-7xl rounded-2xl p-10 mb-16 mx-auto">
          <div className="flex justify-between items-center">
            {statistics.map((stat, index) => (
              <div key={index} className="text-center">
                <div className="flex items-center justify-center mb-4">
                  <stat.icon className="w-12 h-12 text-blue-600 mr-4" />
                  <span className="text-4xl font-bold text-gray-800">{stat.value}</span>
                </div>
                <p className="text-gray-600">{stat.label}</p>
              </div>
            ))}
          </div>
        </section>
        
        {/* Specialists Section */}
        <section className="bg-white rounded-2xl p-8 shadow-md">
          <div className="flex items-center justify-between mb-8">
            <div>
              <h2 className="text-2xl font-bold text-gray-800">8 Physiotherapist Available</h2>   {/*Dymamically change the number of specialists */}
              <p className="text-sm text-gray-500">Updated just now</p>
            </div>
            <div className="flex items-center gap-2 text-sm text-gray-500">
              <Clock className="w-5 h-5" />
              <span>Live Updates</span>
            </div>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
            {physiotherapists.map((physiotherapist, index) => (
              <div 
                key={index} 
                className="bg-gray-50 rounded-2xl overflow-hidden shadow-md hover:shadow-xl transition duration-300 ease-in-out transform hover:-translate-y-2"
              >
                <img 
                  src={`/images/${physiotherapist.image}`} 
                  alt={physiotherapist.name} 
                  className="w-full h-48 object-cover" 
                />
                <div className="p-5">
                  <h3 className="text-lg font-semibold text-gray-800 mb-2">{physiotherapist.speciality}</h3>
                  <p className="text-sm text-gray-600 mb-3">{physiotherapist.name}</p>
                  <div className="flex items-center justify-between">
                    <span className="text-sm text-gray-500">{physiotherapist.slot} Slots Available</span>
                    <button className="text-blue-600 hover:text-blue-800 transition duration-300">
                      <ArrowRight className="w-5 h-5" />
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </section>

                {/* Footer */}
                <footer className="mt-16 bg-gray-800 text-white py-12">
          <div className="container mx-auto px-4 grid grid-cols-1 md:grid-cols-3 gap-8">
            <div>
              <h3 className="text-xl font-bold mb-4">PhysioEase</h3>
              <p className="text-gray-400">Connecting patients with top-tier physiotherapists for personalized care.</p>
            </div>
            <div>
              <h4 className="font-semibold mb-4">Quick Links</h4>
              <ul className="space-y-2">
                <li><a href="#" className="hover:text-blue-400">Find a Physiotherapist</a></li>
                <li><a href="#" className="hover:text-blue-400">Book Appointment</a></li>
                <li><a href="#" className="hover:text-blue-400">Services</a></li>
              </ul>
            </div>
            <div>
              <h4 className="font-semibold mb-4">Contact</h4>
              <p className="text-gray-400">Email: group13@physioease.com</p>
              <p className="text-gray-400">Phone: +1 (555) 123-4567</p>
            </div>
          </div>
          <div className="text-center mt-8 pt-4 border-t border-gray-700">
            <p className="text-sm text-gray-400">&copy; 2024 PhysioEase. All Rights Reserved.</p>
          </div>
        </footer>
      </main>
    </div>
  );
};

export default HomePage;
