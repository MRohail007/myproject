import React, { useState, useEffect } from 'react';
import { useWindowSize } from 'react-use';
import { LineChart, XAxis, YAxis, Tooltip, Legend, Line } from 'recharts';
import Confetti from 'react-confetti';
import { Card, CardContent } from '@/components/ui/card';
import ProgressBar from './ProgressBar';

const ExerciseTypeOverview = ({ data }) => {
  const [showConfetti, setShowConfetti] = useState(false);
  const { width, height } = useWindowSize();

  useEffect(() => {
    if(data.weeklyProgress === 100) {
      setShowConfetti(true);
      const timer = setTimeout(() => {
        setShowConfetti(false);
      }, 5000);
      return () => clearTimeout(timer);
    }
  }, [data.weeklyProgress]);

  return (
    <div>
      <h2 className="text-lg font-medium mb-2">Exercise Overview</h2>
      <Card className="mb-4 border border-blue-500 border-opacity-25">
        <CardContent>
          <ProgressBar progress={data.weeklyProgress} label="Weekly Progress" />
          <div className="text-gray-500 text-sm">
            {data.completedExercises}/{data.totalExercises} exercises completed
          </div>
        </CardContent>
      </Card>
      <LineChart width={500} height={300} data={data.progressData}>
        <XAxis dataKey="name" />
        <YAxis />
        <Tooltip />
        <Legend />
        <Line type="monotone" dataKey="value" stroke="#8884d8" />
      </LineChart>
      {showConfetti && <Confetti width={width} height={height} />}
    </div>
  );
};

export default ExerciseTypeOverview;
