import React, { useRef, useState, useEffect } from 'react';
import { Card, CardContent } from '@/components/ui/card';
import VideoRecorder from './VideoRecorder';
import VideoApiServices from '@/services/videoApiServices';
import { usePatient } from '@/context/PatientContext';
import { usePhysiotherapist } from '@/context/PhysiotherapistContext';
import { X, Play, Pause, Volume2, VolumeX, Maximize, RotateCcw } from 'lucide-react';

const VideoPlayer = ({ videoUrl, onClose, onTimeUpdate }) => {
  const videoRef = useRef(null);
  const progressBarRef = useRef(null);
  const [isRecording, setIsRecording] = useState(false);
  const [uploadStatus, setUploadStatus] = useState("");
  const [isPlaying, setIsPlaying] = useState(false);
  const [currentTime, setCurrentTime] = useState(0);
  const [duration, setDuration] = useState(0);
  const [volume, setVolume] = useState(1);
  const [isMuted, setIsMuted] = useState(false);
  const [showControls, setShowControls] = useState(true);
  const controlsTimeoutRef = useRef(null);
  
  const { patientId } = usePatient();
  const { physiotherapistId } = usePhysiotherapist();
  const videoRecorderRef = useRef();

  /*
  =============
  Send recorded video exercise to physiotherapist
  =============
  */
  const handleCompletedRecording = async (recordBlob) => {
    console.log("Recording completed. Recorded Blog:", recordBlob);
    try {
      const request = await VideoApiServices.sendExerciseVideoToPhysiotherapist(patientId, physiotherapistId, recordBlob);
      setUploadStatus('Video sent to physiotherapist successfully', request);
    } catch (error) {
      console.error("Error uploading video:", error);
      setUploadStatus("Failed to upload video. Please try again.");
    }
  };


    /*
    =============
    Start video recording
    =============
  */
  const handleVideoPlayAndVideoRecord = async () => {
    setIsRecording(true);
  };

  
  /*
  =============
  Video player controls
  =============
  */
  // Toggle play/pause
  const togglePlay = () => {
    if (videoRef.current) {
      if (isPlaying) {
        videoRef.current.pause();
      } else {
        videoRef.current.play();
        if (!isRecording && videoRef.current.currentTime > 0) {
          handleVideoPlayAndVideoRecord();
        }
      }
      setIsPlaying(!isPlaying);
    }
  };

  // Toggle mute
  const toggleMute = () => {
    if (videoRef.current) {
      videoRef.current.muted = !isMuted;
      setIsMuted(!isMuted);
    }
  };

  // Handle volume change
  const handleVolumeChange = (e) => {
    const newVolume = parseFloat(e.target.value);
    if (videoRef.current) {
      videoRef.current.volume = newVolume;
      setVolume(newVolume);
      setIsMuted(newVolume === 0);
    }
  };

  // Handle seeking
  const handleSeek = (e) => {
    if (videoRef.current && progressBarRef.current) {
      const percent = e.nativeEvent.offsetX / progressBarRef.current.offsetWidth;
      videoRef.current.currentTime = percent * videoRef.current.duration;
    }
  };

  // Handle time update
  const handleTimeUpdate = (e) => {
    setCurrentTime(e.target.currentTime);
    onTimeUpdate(e);

    if (!isRecording && e.target.currentTime > 0) {
      handleVideoPlayAndVideoRecord();
    }
  };

  // Format time in MM:SS
  const formatTime = (time) => {
    const minutes = Math.floor(time / 60);
    const seconds = Math.floor(time % 60);
    return `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
  };

  // Toggle fullscreen
  const toggleFullscreen = () => {
    if (videoRef.current) {
      if (document.fullscreenElement) {
        document.exitFullscreen();
      } else {
        videoRef.current.requestFullscreen();
      }
    }
  };

  // Reset video to beginning
  const resetVideo = () => {
    if (videoRef.current) {
      videoRef.current.currentTime = 0;
    }
  };

  // Auto-hide controls after inactivity
  useEffect(() => {
    const handleMouseMove = () => {
      setShowControls(true);

      if (controlsTimeoutRef.current) {
        clearTimeout(controlsTimeoutRef.current);
      }

      controlsTimeoutRef.current = setTimeout(() => {
        if (isPlaying) {
          setShowControls(false);
        }
      }, 3000);
    };

    document.addEventListener('mousemove', handleMouseMove);

    return () => {
      document.removeEventListener('mousemove', handleMouseMove);
      if (controlsTimeoutRef.current) {
        clearTimeout(controlsTimeoutRef.current);
      }
    };
  }, [isPlaying]);



  return (
    <div className="fixed inset-0 bg-black/80 backdrop-blur-sm flex items-center justify-center p-4 z-50">
      <div className="max-w-4xl w-full relative">
        {/* Main video container */}
        <div 
          className="relative group rounded-xl overflow-hidden shadow-2xl bg-black"
          onMouseEnter={() => setShowControls(true)}
          onMouseLeave={() => isPlaying && setShowControls(false)}
        >
          {/* Video element */}
          <video
            ref={videoRef}
            className="w-full aspect-video"
            src={videoUrl}
            onClick={togglePlay}
            onTimeUpdate={handleTimeUpdate}
            onLoadedMetadata={(e) => setDuration(e.target.duration)}
            onPlay={() => setIsPlaying(true)}
            onPause={() => setIsPlaying(false)}
            onError={(e) => {
              console.error('Video playback error:', e);
              onClose();
            }}
            onEnded={() => {
              if (videoRecorderRef.current) {
                videoRecorderRef.current.stopRecording();
              }
              setIsPlaying(false);
              setIsRecording(false);
            }}
          />

          {/* Close button */}
          <button
            onClick={onClose}
            className="absolute top-4 right-4 p-2 bg-black/40 hover:bg-black/60 rounded-full z-10 transition-all"
          >
            <X className="w-5 h-5 text-white" />
          </button>

          {/* Play/Pause overlay */}
          {!isPlaying && (
            <div className="absolute inset-0 flex items-center justify-center">
              <button 
                onClick={togglePlay}
                className="p-5 bg-indigo-600 hover:bg-indigo-700 rounded-full transition-all transform hover:scale-110"
              >
                <Play className="w-8 h-8 text-white" fill="white" />
              </button>
            </div>
          )}

          {/* Video controls - shows based on showControls state */}
          <div 
            className={`absolute bottom-0 left-0 right-0 bg-gradient-to-t from-black/70 to-transparent p-4 transition-opacity duration-300 ${showControls ? 'opacity-100' : 'opacity-0'}`}
          >
            {/* Progress bar */}
            <div 
              ref={progressBarRef}
              className="w-full h-2 bg-gray-500/50 rounded-full overflow-hidden cursor-pointer mb-3"
              onClick={handleSeek}
            >
              <div 
                className="h-full bg-indigo-500"
                style={{ width: `${(currentTime / duration) * 100}%` }}
              ></div>
            </div>

            {/* Controls row */}
            <div className="flex items-center justify-between">
              <div className="flex items-center space-x-4">
                {/* Play/Pause button */}
                <button 
                  onClick={togglePlay}
                  className="text-white hover:text-indigo-300 transition-colors"
                >
                  {isPlaying ? <Pause className="w-5 h-5" /> : <Play className="w-5 h-5" />}
                </button>

                {/* Reset button */}
                <button 
                  onClick={resetVideo}
                  className="text-white hover:text-indigo-300 transition-colors"
                >
                  <RotateCcw className="w-4 h-4" />
                </button>

                {/* Volume controls */}
                <div className="flex items-center space-x-2">
                  <button 
                    onClick={toggleMute}
                    className="text-white hover:text-indigo-300 transition-colors"
                  >
                    {isMuted ? <VolumeX className="w-5 h-5" /> : <Volume2 className="w-5 h-5" />}
                  </button>
                  <input
                    type="range"
                    min="0"
                    max="1"
                    step="0.1"
                    value={isMuted ? 0 : volume}
                    onChange={handleVolumeChange}
                    className="w-20 accent-indigo-500"
                  />
                </div>

                {/* Time display */}
                <div className="text-white text-sm">
                  {formatTime(currentTime)} / {formatTime(duration)}
                </div>
              </div>

              <div className="flex items-center space-x-3">
                {/* Fullscreen button */}
                <button 
                  onClick={toggleFullscreen}
                  className="text-white hover:text-indigo-300 transition-colors"
                >
                  <Maximize className="w-5 h-5" />
                </button>
              </div>
            </div>
          </div>
        </div>

        {/* Recording status indicator */}
        {isRecording && (
          <div className="mt-4 flex items-center bg-red-600 text-white px-3 py-2 rounded-lg text-sm font-medium w-fit">
            <div className="w-3 h-3 bg-white rounded-full mr-2 animate-pulse"></div>
            Recording in progress
          </div>
        )}

        {/* Upload Status Message */}
        {uploadStatus && (
          <div className="mt-4 bg-white/10 backdrop-blur-sm text-white px-4 py-3 rounded-lg text-sm">
            {uploadStatus}
          </div>
        )}

        {/* VideoRecorder component */}
        {isRecording && (
          <div className="mt-4">
            <VideoRecorder
              ref={videoRecorderRef}
              onRecordingComplete={handleCompletedRecording}
            />
          </div>
        )}
      </div>
    </div>
  );
};

export default VideoPlayer;
