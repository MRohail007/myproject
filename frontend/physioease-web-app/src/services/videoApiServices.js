import {apiClient} from "./apiClient";
  /*
  ==========
  Note: This file contains all the services related to the video API
  The services include: fetchVideos, playVideo, fetchAssignedVideos, markVideoAsCompleted, fetchProgressData
  Please note that the patientId is hardcoded to 2 for now, this should be dynamic in the future
  When the patient logs in, the patientId should be fetched from the user's data
  After we have created the user authentication system on the backend
  ==========
  */
class VideoApiServices {
  static async fetchVideos(patientId) {
//    const patientId = 2;

    try {
      const response = await apiClient.get(`/patient/${patientId}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  }
  /*
  ==========
  Function for fetching video to play
  ==========
  */
  static async playVideo(videoId) {
    try {
      const response = await apiClient.get(`/videos/${videoId}/play`, {
        responseType: 'blob',
      });
      return response.data;
    } catch (error) {
      throw error;
    }
  }
    /*
  ==========
  fetchAssignedVideos
  ==========
  */
  static async fetchAssignedVideos(patientId) {
    try {
      const response = await apiClient.get(`/patient/${patientId}`);
      return response.data;
    } catch (error) {
      console.error("Error fetching videos:", error)
      throw error;
    }
  }
  /*
  ==========
  markVideoAsCompleted
  ==========
  */
  static async markVideoAsCompleted(videoId, patientId) {
    try {
      const response = await apiClient.put(`/completed/${patientId}`, {
        id: videoId,
        isCompleted: true,
      });
      if (response.status !== 200) { // Now returning data from the server
        throw new Error('Failed marking video as completed, Internal Server Error', response);
      }
      //alert('Exercise marked as completed'); // Todo: Remove this alert and render a UI message
    } catch (error) {
      console.error("Error marking video as completed:", error);
      alert('Error marking video as completed');
    }
  }
    /*
  ==========
  fetchProgressData
  ==========
  */
  static async fetchProgressData(patientId) {
    try {
      const response = await apiClient.get(`/${patientId}/progress`);
      return response.data;
    }catch (error){
      console.error("Error fetching progress:", error);
      throw error;
    }
  }

  static async fetchVideoProgressData(videoId, patientId) {
    try {
      const VideoProgressresponse = await apiClient.get(`/${videoId}/progress/${patientId}`);
      return VideoProgressresponse.data;
    }catch (error){
      console.error("Error fetching progress data for video completion:", error);
      throw error;
    }
  }

  static async fetchPatientProgressData(patientId) {
    try {
      const progressDataResponse = await apiClient.get(`/progress-tracking/${patientId}`);
      return progressDataResponse.data;
    }catch (error) {
      console.error("Error fetching patient chart progress data;", error)
    }
  }

  static async videoPlaybackProgress(videoId, progress) {
    try {
      const progressResponse = await apiClient.post(`/update-playback-progress`, null, {
        params: {videoId, progress}
      });
      return progressResponse.data;
    } catch(error) {
        console.error("Error updating video playback progress:", error);
        throw error;
      }
    }

    static async fetchAchievements(patientId) {
      try {
        const achievementsResponse = await apiClient.get(`/achievements/patient/${patientId}`);
      return achievementsResponse.data;
    } catch (error) {
      console.error("Error fetching achievements:", error);
      throw error;
    }
  }

  static async sendExerciseVideoToPhysiotherapist(patientId, physiotherapistId, videoFile) {
    try {
      const videoFormData = new FormData();
      videoFormData.append('patientId', patientId);
      videoFormData.append('physiotherapistId', physiotherapistId);
      videoFormData.append('file', videoFile);

      // Logging the patientID and physioID
      for (let [key, value] of videoFormData.entries()) {
        console.log(key, value);
      }

      // Send the form data to the server
      const response = await apiClient.post('/upload-exercise-video', videoFormData, {
        headers: {
          'Content-type': 'multipart/form-data',
        },
      });
      console.log('Video uploaded successfully:', response.data);
      return response.data;
    } catch (error) {
      console.error("Error sending exercise video to physiothherapist:", error.response?.data || error.message);
      throw error;
    }
  }
}


export default VideoApiServices;
