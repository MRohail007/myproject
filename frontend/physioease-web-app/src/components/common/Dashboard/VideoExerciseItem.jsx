import React from 'react'
import { Lock, Unlock, Circle } from 'lucide-react'
import Modal from '@/components/ui/alertModal'
import { useState } from 'react'

const ExerciseItem = ({
  day,
  videoId,
  exercise,
  status,
  resourceUrl,
  onPlay,
  isCompleted,
  onCheckboxChange,
  isSkipped,
}) => {
  // Get the current day of the week
  const getCurrentDay = () => {
    const daysOfWeek = [
      'Sunday',
      'Monday',
      'Tuesday',
      'Wednesday',
      'Thursday',
      'Friday',
      'Saturday',
    ]
    const today = new Date()
    return daysOfWeek[today.getDay()]
  }
  const currentDay = getCurrentDay()
  // If the video is not completed and its is the crrent day then patient can pplay the video
  // Log the values of `day` and `currentDay` for debugging
  //  console.log(`Video Day: ${day}, Current Day: ${currentDay}`);

  // Determine if the video is playable based on the current day
  const isPlayable = day === currentDay && !isCompleted
  console.log(`Is Playable: ${isPlayable}`)

  const [showVideoAlert, setShowVideoAlert] = useState(false)

  const handlePlayClick = () => {
    setShowVideoAlert(true)
  }

  const handleVideoAlertConfirmation = () => {
    setShowVideoAlert(false)
    onPlay(resourceUrl)
  }

  const handleCheckboxChange = async (event) => {
    const checked = event.target.checked
    onCheckboxChange(videoId, checked)
  }

  return (
    <div className="flex items-center p-3 bg-gray-50 rounded-lg mb-2">
      {/* Exercise Name */}
      <div className="font-medium flex-1">{exercise}</div>

      {/* Status Button and Checkbox Group */}
      <div className="flex items-center gap-4">
        {/* Status Button */}
        <button
          className={`text-white px-4 py-2 rounded flex items-center ${
            isPlayable
              ? 'bg-green-500 hover:bg-green-600'
              : isCompleted
                ? 'bg-blue-500 hover:bg-blue-600'
                : isSkipped
                  ? 'bg-yellow-500 hover:bg-yellow-600'
                  : 'bg-red-500 hover:bg-red-600'
          }`}
          onClick={handlePlayClick} // return friendly user message
          disabled={!isPlayable}
        >
          {isPlayable ? (
            <>
              <Unlock className="mr-2" size={16} />
              Play
            </>
          ) : isCompleted ? (
            <>
              <Circle className="mr-2" size={16} />
              Completed
            </>
          ) : isSkipped ? (
            <>
              <Circle className="mr-2" size={16} />
              Skipped
            </>
          ) : (
            <>
              <Lock className="mr-2" size={16} />
              Locked
            </>
          )}
        </button>

        {/* Checkbox with Label */}
        <label className="flex items-center whitespace-nowrap">
          <input
            type="checkbox"
            checked={isCompleted}
            onChange={handleCheckboxChange}
            className="mr-2 h-4 w-4 rounded border-gray-300 text-blue-600 focus:ring-blue-500"
          />
          <span className="text-sm text-gray-600">Completed</span>
        </label>
        <Modal
          isOpen={showVideoAlert}
          onClose={handleVideoAlertConfirmation}
          title="Important!"
          message="The webcam recording will start immediately when the video begins playing. Please get ready!"
        />
        {/* {!isCompleted && !isSkipped && (
          <button
            className="text-sm text-gray-600 hover:text-gray-800 underline"
            onClick={() => onSkip(videoId)}
          >
            Skip
          </button>
        )} */}
      </div>
    </div>
  )
}

export default ExerciseItem
