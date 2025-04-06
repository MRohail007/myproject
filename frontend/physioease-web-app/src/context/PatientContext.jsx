import React,  {createContext, useContext, useState } from "react";

const PatientContext = createContext();
 export const usePatient = () => {
  return useContext(PatientContext);
 };

 export const PatientProvider = ({ children}) => {
  const [patientId, setPatientId] = useState(2);

  return (
    <PatientContext.Provider value={{ patientId, setPatientId}}>
      {children}
    </PatientContext.Provider>
  );
 }
