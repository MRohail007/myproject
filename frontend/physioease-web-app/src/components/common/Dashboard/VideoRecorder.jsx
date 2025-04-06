import React, { useRef, useState } from "react";

const VideoRecorder = ({ onRecordingComplete }) => {
  const webcamVideoRef = useRef(null);
  const mediaRecorderRef = useRef(null);
  const [isRecording, setIsRecording] = useState(false);

  // Start recording
  const startRecording = async () => {
    try {
      // Request webcam access
      const stream = await navigator.mediaDevices.getUserMedia({
        video: true,
        audio: true,
      });

      // Set up webcam preview
      if (webcamVideoRef.current) {
        webcamVideoRef.current.srcObject = stream;
      }

      // Initialize MediaRecorder
      const mediaRecorder = new MediaRecorder(stream);
      mediaRecorderRef.current = mediaRecorder;

      // Collect recorded chunks
      const chunks = [];
      mediaRecorder.ondataavailable = (event) => {
        if (event.data.size > 0) {
          chunks.push(event.data);
        }
      };

      // When recording stops, notify the parent
      mediaRecorder.onstop = () => {
        const blob = new Blob(chunks, { type: "video/webm" });
        onRecordingComplete(blob); // Pass the recorded video to the parent

        // Stop all tracks in the stream to release the webcam
        if (stream) {
          stream.getTracks().forEach((track) => track.stop());
        }
      };

      mediaRecorder.start();
      setIsRecording(true);
    } catch (error) {
      console.error("Error accessing the media device:", error);
    }
  };

  // Stop recording
  const stopRecording = () => {
    if (mediaRecorderRef.current && isRecording) {
      mediaRecorderRef.current.stop(); // This triggers the `onstop` event
      setIsRecording(false);
    }
  };

  return (
    <div>
      <h1>Exercise Webcam</h1>

      {/* Start Recording Button */}
      <button
        onClick={startRecording}
        disabled={isRecording}
        style={{
          padding: "10px 20px",
          backgroundColor: "#4CAF50",
          color: "white",
          border: "none",
          borderRadius: "5px",
          cursor: "pointer",
          marginBottom: "20px",
        }}
      >
        {isRecording ? "Recording..." : "Start Recording"}
      </button>

      {/* Stop Recording Button */}
      <button
        onClick={stopRecording}
        disabled={!isRecording}
        style={{
          padding: "10px 20px",
          backgroundColor: "#f44336",
          color: "white",
          border: "none",
          borderRadius: "5px",
          cursor: "pointer",
          marginBottom: "20px",
          marginLeft: "10px",
        }}
      >
        Stop Recording
      </button>

      {/* Webcam Preview */}
      <video
        ref={webcamVideoRef}
        autoPlay
        playsInline
        muted
        style={{
          width: "100%",
          maxWidth: "500px",
          border: "3px solid #ccc",
        }}
      />
    </div>
  );
};

export default VideoRecorder;
