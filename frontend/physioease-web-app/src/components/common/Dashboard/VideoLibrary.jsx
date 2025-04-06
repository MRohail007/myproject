import React, { useState } from 'react';
import { Card, CardContent } from '@/components/ui/card';
import ExerciseItem from './VideoExerciseItem';
import VideoUpload from '@/components/common/Dashboard/videoUpload';
import { ChevronRight, Calendar, CheckCircle } from 'lucide-react';

const ExerciseLibrary = ({
  groupedExercises,
  daysOrder,
  onPlay,
  onComplete,
  patientId,
  videoId,
  onCheckboxChange,
  handleVideoUpload
}) => {
  const physiotherapistId = '1';
  const [expandedDay, setExpandedDay] = useState(daysOrder[0] || '');
  
  // Calculate completion percentage for a day
  const getCompletionPercent = (exercises) => {
    if (!exercises || exercises.length === 0) return 0;
    const completed = exercises.filter(ex => ex.isCompleted).length;
    return Math.round((completed / exercises.length) * 100);
  };
  
  return (
    <div className="h-full flex flex-col bg-gradient-to-br from-blue-50 to-indigo-50 p-6 rounded-xl">
      <h2 className="text-2xl font-bold text-blue-900 mb-6 flex items-center">
        <Calendar className="mr-2 h-6 w-6 text-blue-600" />
        Exercise Program
      </h2>
      
      <div className="overflow-y-auto max-h-96 pr-2 space-y-4 custom-scrollbar">
        {daysOrder.map(day => {
          const exerciseForDay = groupedExercises[day];
          if (!exerciseForDay) return null;
          
          const isExpanded = expandedDay === day;
          const completionPercent = getCompletionPercent(exerciseForDay);
          
          return (
            <Card 
              key={day}
              className={`transition-all duration-300 hover:shadow-lg ${isExpanded ? 'border-indigo-400 shadow-md' : 'border-gray-200'}`}
            >
              <div 
                className={`px-4 py-3 cursor-pointer flex items-center justify-between ${isExpanded ? 'bg-indigo-50' : 'bg-white'}`}
                onClick={() => setExpandedDay(isExpanded ? '' : day)}
              >
                <div className="flex items-center">
                  <span className="w-8 h-8 flex items-center justify-center rounded-full bg-indigo-100 text-indigo-800 font-semibold mr-3">
                    {day.slice(0, 1)}
                  </span>
                  <h3 className="text-lg font-medium text-gray-900">{day}</h3>
                </div>
                
                <div className="flex items-center">
                  <div className="mr-4 flex items-center">
                    <div className="w-32 h-2 bg-gray-200 rounded-full overflow-hidden">
                      <div 
                        className="h-full bg-green-500 rounded-full"
                        style={{ width: `${completionPercent}%` }}
                      ></div>
                    </div>
                    <span className="ml-2 text-sm text-gray-600">{completionPercent}%</span>
                  </div>
                  <ChevronRight className={`transform transition-transform duration-200 ${isExpanded ? 'rotate-90' : ''} text-gray-400`} />
                </div>
              </div>
              
              {isExpanded && (
                <CardContent className="pt-4 pb-3">
                  <div className="space-y-4">
                    {exerciseForDay.map((exercise) => (
                      <div key={exercise.id} className="bg-white rounded-lg p-3 shadow-sm hover:shadow transition-shadow duration-200">
                        <ExerciseItem
                          exercise={exercise.title}
                          day={day}
                          status={exercise.status}
                          resourceUrl={exercise.resourceUrl}
                          onPlay={onPlay}
                          isCompleted={exercise.isCompleted}
                          onComplete={onComplete}
                          patientId={patientId}
                          videoId={videoId}
                          onCheckboxChange={onCheckboxChange}
                        />
                        
                        <div className="mt-3 pt-3 border-t border-gray-100">
                          <VideoUpload 
                            patientId={patientId} 
                            physiotherapistId={physiotherapistId} 
                            onUpload={handleVideoUpload} 
                          />
                        </div>
                      </div>
                    ))}
                  </div>
                </CardContent>
              )}
            </Card>
          );
        })}
      </div>
      
      <style jsx>{`
        .custom-scrollbar::-webkit-scrollbar {
          width: 6px;
        }
        .custom-scrollbar::-webkit-scrollbar-track {
          background: rgba(0, 0, 0, 0.05);
          border-radius: 10px;
        }
        .custom-scrollbar::-webkit-scrollbar-thumb {
          background: rgba(79, 70, 229, 0.3);
          border-radius: 10px;
        }
        .custom-scrollbar::-webkit-scrollbar-thumb:hover {
          background: rgba(79, 70, 229, 0.5);
        }
      `}</style>
    </div>
  );
};

export default ExerciseLibrary;
