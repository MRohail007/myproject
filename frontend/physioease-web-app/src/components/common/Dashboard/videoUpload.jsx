import React, { useState } from 'react';

import { Camera, Upload } from 'lucide-react';
import VideoApiServices from '@/services/videoApiServices';
import { UploadCloud, Trash2 } from 'lucide-react';
import { usePatient } from '@/context/PatientContext';


// const VideoUpload = ({ onUpload }) => {
//   const handleFileChange = (event) => {
//     const file = event.target.files[0];
//     if (file) {
//       onUpload(file);
//     }
//   };

const VideoUpload = ({ patientId, physiotherapistId }) => {
  const [videoFile, setVideoFile] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [uploadComplete, setUploadComplete] = useState(false);
  const [progress, setProgress] = useState(0); // Track upload progress
  const [error, setError] = useState(null);

  /*
  =======
  Function to handle video change
  =======
  */
  const handleVideoChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      setVideoFile(file);
      setUploadComplete(true);
      setProgress(0);
    };
  }

  /*
  =======
  Function to handle video upload
  =======
  */
  const handleVideoUpload = async () => {
    if (!videoFile) {
      // Use the created or custom alert component
      alert('Please select a video file to upload');
      return;
    }
    setUploading(true);
    setError(null);
    setProgress(0);

    /*
    ==========
    Simulate video upload
    ==========
    */
    try {
      //Similate uplaod progress 
      const uploadInterval = setInterval(() => {
        setProgress((prevProgress) => {
          if (prevProgress >= 100) {
            clearInterval(uploadInterval);
            return 100;
          }
          return prevProgress + 10; // Increment progress by 10% every interval
        });
      }, 500);
      const request = await VideoApiServices.sendExerciseVideoToPhysiotherapist(patientId, physiotherapistId, videoFile);
      console.log('Video upload successfully: ', request);

      setUploadComplete(true);
    } catch (error) {
      console.error('Error uploading video:', error);
      setError(error.message || 'An error occurred while uploading the video');
    } finally {
      setUploading(false);
    }
  };
  // Clear uploaded file and reset state
  const handleClearFile = () => {
    setVideoFile(null);
    setUploadComplete(false);
    setProgress(0);
    setError(null);
  };

  return (
    <div className="mt-4">
      <label className="flex items-center gap-2 p-4  rounded-lg cursor-pointer hover:bg-blue-100">
        <Upload className="h-6 w-6" />
        <span>Upload your video</span>
        <input
          type="file"
          accept="video/*"
          className="hidden"
          onChange={handleVideoChange}
        />
        {error && <p>Error: {error}</p>}
      </label>

      {/* Progress Indicator for large file video upload*/}
      {uploading && (
        <div className="mt-2">
          <div className="relative h-2 w-full bg-gray-200 rounded-full overflow-hidden">
            <div
              className="absolute top-0 left-0 h-full bg-blue-500 transition-all duration-300"
              style={{ width: `${progress}%` }}
            ></div>
          </div>
          <p className="text-xs text-gray-600 mt-1">Uploading... {progress}%</p>
        </div>
      )}

      {/* Clear Button */}
      {videoFile && (
        <button
          className="mt-4 px-4 py-2 rounded-md bg-red-500 hover:bg-red-600 text-white flex items-center gap-2"
          onClick={handleClearFile}
        >
          <Trash2 className="h-4 w-4" />
          Clear File
        </button>
      )}
      {/* Submit Button */}
      <button
        className={`mt-4 px-4 py-2 rounded-md ${uploadComplete ? 'bg-green-500 hover:bg-green-600' : 'bg-gray-400 cursor-not-allowed'
          } text-white`}
        disabled={!uploadComplete} // Disable the button until upload is complete
        onClick={handleVideoUpload}
      >
        Submit Video
      </button>
    </div>
  );
};

export default VideoUpload;
