import axios from "axios";

const API_BASE_URL = "http://localhost:8085/api/video";
const PHYSIO_API_BASE_URL = "http://localhost:8085";

export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
});

export const physiotherapistApi = axios.create({
  baseURL: PHYSIO_API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
});

