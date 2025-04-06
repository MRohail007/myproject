import { physiotherapistApi } from "./apiClient";


class physiotherapistApiServices {

  static async fetchPhysiotherapists() {
    console.log("Fetching physiotherapists...");
    try {

      const response = await physiotherapistApi.get(`/api/physiotherapists`);
      console.log("Physiotherapists fetched:", response.data);

      return response.data;


    } catch (error) {
      if (error.response) {
        console.error("Error fetching physiotherapists:", error.response.data);
        throw error.response.data;
      } else {
        console.error("Error fetching physiotherapists:", error.message);
        throw error;
      }
    }
  };
}

export default physiotherapistApiServices;
