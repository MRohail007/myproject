import { useState, useEffect } from 'react';
import VideoApiServices from '@/services/videoApiServices';

const useExerciseData = (patientId) => {
  const [exercises, setExercises] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [progressData, setProgressData] = useState([]);
  const [achievements, setAchievements] = useState([]);

  const fetchData = async () => {
    setLoading(true);
    setError(null);
    try {
      const [exercisesResponse, progressResponse, achievementsResponse] = await Promise.all([
        VideoApiServices.fetchAssignedVideos(patientId),
        VideoApiServices.fetchPatientProgressData(patientId),
        VideoApiServices.fetchAchievements(patientId),
      ]);
      
      setExercises(exercisesResponse);
      const transformedProgressData = progressResponse.map(item => ({
        name: item.category,
        value: item.totalPlaybackProgress
      }));
      setProgressData(transformedProgressData);
      setAchievements(achievementsResponse);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [patientId]);

  return { exercises, loading, error, progressData, achievements, refetch: fetchData };
};

export default useExerciseData;
