import React, { useState, useEffect } from 'react'
import throttle from 'lodash.throttle'
// UI Components
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card'
import Modal from '../ui/alertModal'
// Hooks and Context
import { usePatient } from '../../context/PatientContext'
import useExerciseData from '@/hooks/useExerciseData'
// Services
import VideoApiServices from '@/services/videoApiServices'

// Dashboard Components
import BookedPhysiotherapistInfo from '../common/Dashboard/BookedPhysiotherapist'
import ProgressBar from '../common/Dashboard/ProgressBar'
import ExerciseProgress from '../common/Dashboard/Achievements'
import VideoPlayer from '../common/Dashboard/VideoPlayer'
import CelebrationModal from '../common/Dashboard/CelebrationModal'
import ExerciseTypeOverview from '../common/Dashboard/ExerciseTypeOverview'
import ExerciseLibrary from '../common/Dashboard/VideoLibrary'

const PatientDashboard = () => {
  // Context and Custom Hook State
  const { patientId } = usePatient()
  const { exercises, loading, error, progressData, achievements, refetch } =
    useExerciseData(patientId)

  // Video Player State
  const [currentVideoUrl, setCurrentVideoUrl] = useState(null)
  const [currentResourceUrl, setCurrentResourceUrl] = useState(null)
  const [videoId, setVideoId] = useState(null)

  // UI State
  const [showCelebration, setShowCelebration] = useState(false)
  const [showModal, setShowModal] = useState(false)
  const [modalMessage, setModalMessage] = useState('')

  // Constants and Data Organization
  // Define the order of days for consistent display
  const daysOrder = [
    'Monday',
    'Tuesday',
    'Wednesday',
    'Thursday',
    'Friday',
    'Saturday',
    'Sunday',
  ]

  // Group exercises by day for organized display
  const groupedExercises = exercises.reduce((acc, exercise) => {
    if (!acc[exercise.day]) {
      acc[exercise.day] = []
    }
    acc[exercise.day].push(exercise)
    return acc
  }, {})

  // Extract video ID from resource URL whenever it changes
  useEffect(() => {
    if (currentResourceUrl) {
      const videoIdMatch = currentResourceUrl.match(/\/videos\/(\d+)\/play/)
      const extractedVideoId = videoIdMatch ? videoIdMatch[1] : null
      setVideoId(extractedVideoId)
    }
  }, [currentResourceUrl])

  // Calculate total exercises and completion metrics
  const totalExercises = exercises.length
  const completedExercises = exercises.filter((ex) => ex.isCompleted).length
  const weeklyProgress = (completedExercises / totalExercises) * 100

  // Handle video playback initialization
  const handlePlay = async (resourceUrl) => {
    try {
      const videoPath = resourceUrl.replace('/api/video', '')
      const fullUrl = `http://localhost:8085/api/video${videoPath}`
      setCurrentVideoUrl(fullUrl)
      setCurrentResourceUrl(resourceUrl)
    } catch (error) {
      console.error('Error loading video:', error)
    }
  }

  // Track video progress and update backend (throttled to prevent excessive calls)
  const handleTimeUpdate = throttle((e) => {
    const videoElement = e.target
    const progress = (videoElement.currentTime / videoElement.duration) * 100
    if (videoId) {
      VideoApiServices.videoPlaybackProgress(videoId, progress)
    }
  }, 10000)

  const handleComplete = (videoId, checked) => {
    console.log(
      `Exercise ${videoId} marked as ${checked ? 'completed' : 'not completed'}`
    )
    // Add logic to update state or make an API call
  }

  // Handle exercise completion marking
  const handleCheckboxChange = async (videoId, checked) => {
    try {
      if (videoId) {
        // Fetch the progress data
        const progressResponse = await VideoApiServices.fetchVideoProgressData(
          videoId,
          patientId
        )
        const videoProgress = progressResponse.playbackprogress
        console.log(`video progress value: ${videoProgress}`)
        console.log('typeof videoProgress:', typeof videoProgress)

        // Check if the progress is greater then or equal to 50
        if (videoProgress >= 50) {
          await VideoApiServices.markVideoAsCompleted(videoId, patientId)
          handleComplete(videoId, checked)
          refetch()
        } else {
          setModalMessage(
            'Please play at least 50% of the video before marking it as completed.'
          )
          setShowModal(true)
        }
      } else {
        setModalMessage(
          'Please play the video first before marking it as completed.'
        )
        setShowModal(true)
      }
    } catch (error) {
      console.error('Error marking video as completed:', error)
    }
  }

  // Handle video file uploads
  const handleVideoUpload = async (file) => {
    console.log('Uploading video file: ', file)
  }

  // Show loading indicator while data is being fetched
  if (loading)
    return (
      <div className="flex items-center justify-center h-screen">
        Loading...
      </div>
    )

  // Component Render
  return (
    <Card className="w-full max-h-screen overflow-auto ">
      {/* Header Section */}
      <CardHeader className="flex justify-between">
        <CardTitle className="text-center">Patient Dashboard</CardTitle>
        <BookedPhysiotherapistInfo />
      </CardHeader>

      <CardContent className="space-y-6">
        {/* Progress Overview Section */}
        <div className="space-y-4">
          <ProgressBar progress={80} label="Your Monthly Progress" />
          <ExerciseProgress achievements={achievements} />
        </div>

        {/* Main Content Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Exercise Library Section */}
          <ExerciseLibrary
            groupedExercises={groupedExercises}
            daysOrder={daysOrder}
            onPlay={handlePlay}
            patientId={patientId}
            videoId={videoId}
            onCheckboxChange={handleCheckboxChange}
            handleVideoUpload={handleVideoUpload}
          />

          {/* Exercise Overview Section */}
          <ExerciseTypeOverview
            data={{
              progressData,
              weeklyProgress,
              completedExercises,
              totalExercises,
            }}
          />
        </div>

        {/* Modals and Overlays */}
        {currentVideoUrl && (
          <VideoPlayer
            videoUrl={currentVideoUrl}
            onClose={() => {
              setCurrentVideoUrl(null)
              setCurrentResourceUrl(null)
            }}
            onTimeUpdate={handleTimeUpdate}
          />
        )}
        <CelebrationModal
          isOpen={showCelebration}
          onClose={() => setShowCelebration(false)}
        />
        <Modal
          isOpen={showModal}
          onClose={() => setShowModal(false)}
          title="Information!!!"
          message={modalMessage}
        />
        <PoseDetection />
      </CardContent>
    </Card>
  )
}
export default PatientDashboard
