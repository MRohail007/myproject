import React, { useState } from 'react';
import { Camera, Award, Upload } from 'lucide-react';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';
import { Alert, AlertDescription } from '@/components/ui/alert';

const ExerciseProgress = ({ achievements }) => {
  const [photos, setPhotos] = useState([]);

  const handlePhotoUpload = (e) => {
    const file = e.target.files[0];
    if (file) {
      const newPhoto = {
        id: photos.length + 1,
        date: new Date().toLocaleDateString(),
        url: URL.createObjectURL(file)
      };
      setPhotos([...photos, newPhoto]);

      if (photos.length + 1 === 5) {
        const updatedAchievements = achievements.map(achievement => 
          achievement.id === 5 ? { ...achievement, earned: true } : achievement
        );
        setAchievements(updatedAchievements);
      }
    }
  };

  return (
    <div className="space-y-6">
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <Award className="h-6 w-6" />
            Achievements
          </CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {achievements.map((achievement) => (
              <div
                key={achievement.id}
                className={`p-4 rounded-lg border ${
                  achievement.earned ? 'bg-blue-50 border-blue-200' : 'bg-gray-50 border-gray-200'
                }`}
              >
                <div className="flex items-center gap-3">
                  <span className="text-2xl">{achievement.icon}</span>
                  <div>
                    <h3 className="font-semibold">{achievement.description}</h3>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <Camera className="h-6 w-6" />
            Progress Videos
          </CardTitle>
        </CardHeader>
        <CardContent>
          <div className="mb-4">
            <label className="flex items-center gap-2 p-4 border-2 border-dashed rounded-lg cursor-pointer hover:bg-gray-50">
              <Upload className="h-6 w-6" />
              <span>Upload new Video</span>
              <input
                type="file"
                accept="image/*"
                className="hidden"
                onChange={handlePhotoUpload}
              />
            </label>
          </div>

          {photos.length > 0 ? (
            <div className="grid grid-cols-2 md:grid-cols-3 gap-4">
              {photos.map((photo) => (
                <div key={photo.id} className="relative">
                  <img
                    src={photo.url}
                    alt={`Progress photo ${photo.date}`}
                    className="w-full h-48 object-cover rounded-lg"
                  />
                  <span className="absolute bottom-2 right-2 bg-black bg-opacity-50 text-white text-xs px-2 py-1 rounded">
                    {photo.date}
                  </span>
                </div>
              ))}
            </div>
          ) : (
            <Alert>
              <AlertDescription>
                No progress photos uploaded yet. Upload your first photo to track your journey!
              </AlertDescription>
            </Alert>
          )}
        </CardContent>
      </Card>
    </div>
  );
};

export default ExerciseProgress;
