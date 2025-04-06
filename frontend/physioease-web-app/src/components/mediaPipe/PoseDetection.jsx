import react, { useRef, useEffect, useState } from 'react'
import { Pose } from '@mediapipe/pose'
import * as cam from '@mediapipe/camera_utils'
import * as drawingUtils from '@mediapipe/drawing_utils'
import * as mediapipePose from '@mediapipe/pose'

function PoseDetection() {
  const videoRef = useRef(null)
  const canvasRef = useRef(null)
  const [poseData, setPoseData] = useState(null)
  const [camera, setCamera] = useState(null)

  useEffect(() => {
    const initializePose = async () => {
      if (!videoRef.current || !canvasRef.current) return // what are we returning

      // Get the video and the canvas
      const video = videoRef.current
      const canvas = canvasRef.current
      const ctx = canvas.getContext('2d') // ctx is the canvas context that provides methods and properties to draw on a 2D canvas;

      const pose = new Pose({
        locateFile: (file) => {
          // Creates new instance of MediaPipe Pose model
          // locateFiles callback tells it where to load model files from CDN
          // Returns URL for each requested model file from jsdelivr npm CDN
          //return `https://cdn.jsdelivr.net/npm/@mediapipe/pose/${file}`
          //return `https://cdn.jsdelivr.net/npm/@mediapipe/pose@0.2/${file}`
          return `https://cdn.jsdelivr.net/npm/@mediapipe/pose@0.5.1675469404/${file}`
        },
      })

      pose.setOptions({
        modelComplexity: 1,
        smoothLandmarks: true,
        enableSegmentation: false,
        smoothSegmentation: false,
        minDetectionConfidence: 0.5,
        minTrackingConfidence: 0.5,
      })

      await pose.initialize()

      pose.onResults((results) => {
        if (!ctx) return

        ctx.save()
        ctx.clearRect(0, 0, canvas.width, canvas.height)

        // Draw the pose landmark on canvas
        if (results.poseLandmarks) {
          drawingUtils.drawConnectors(
            ctx,
            results.poseLandmarks,
            mediapipePose.POSE_CONNECTIONS,
            { color: '00FF00', lineWidth: 4 }
          )

          drawingUtils.drawLandmarks(ctx, results.poseLandmarks, {
            color: '#FF0000',
            lineWidth: 2,
          })
          // set the poseData for sending over to the backend server
          setPoseData(results.poseLandmarks)
        }
      })

      // This code initializes a camera instance using MediaPipe camera utility
      // It takes the video element reference and configures:
      // - An onFrame handler that runs pose detection on each frame
      // - Width and height dimensions for the camera feed
      const cameraInstance = new cam.Camera(video, {
        onFrame: async () => {
          try {
            await pose.send({ image: video })
          } catch (error) {
            console.error('Error in pose detection:', error)
          }
        },
        width: 640,
        height: 400,
      })

      try {
        await cameraInstance.start()
        setCamera(cameraInstance)
      } catch (error) {
        console.error('Error Starting camera: ', error)
      }

      // This cleanup function runs when component unmounts
      // It stops the camera feed and closes the pose detection
      // return () => {
      //   if (cameraInstance) {
      //     cameraInstance.stop()
      //   }
      //   pose.close()
      // }

      return () => {
        if (camera) {
          camera.stop()
        }
        pose.close()
      }
    }
    initializePose()
  }, [])

  // Function to print pose data
  //Todo: Send data to the backend instead of printing out the data
  const sendPoseDataToBackend = () => {
    if (!poseData) return
    console.log('Current pose data:', poseData)
  }

  const sendPoseDatasToBackend = async () => {
    if (!poseData) return

    try {
      const response = await fetch('http://localhost:8080/api/pose-data', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ poseData }),
      })

      const data = await response.json()
      console.log('Response from backend:', data)
    } catch (error) {
      console.error('Error sending pose data to backend:', error)
    }
  }

  return (
    <div className="pose-detection">
      <div style={{ position: 'relative' }}>
        <video
          ref={videoRef}
          style={{
            width: '640px',
            height: '480px',
            position: 'absolute',
            top: 0,
            left: 0,
          }}
        />
        <canvas
          ref={canvasRef}
          width={640}
          height={480}
          style={{
            width: '640px',
            height: '480px',
            position: 'absolute',
            top: 0,
            left: 0,
          }}
        />
      </div>
      <button onClick={sendPoseDataToBackend} style={{ marginTop: '490px' }}>
        Analyze Pose
      </button>
    </div>
  )
}

export default PoseDetection
