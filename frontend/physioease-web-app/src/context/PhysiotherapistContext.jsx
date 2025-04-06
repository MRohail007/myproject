import physiotherapistApiServices from "@/services/physiotherapistApiServices";
import VideoApiServices from "@/services/videoApiServices";
import React,  { Children, createContext, useContext, useEffect, useState } from "react";

const PhysiotherapistContext = createContext();

export const PhysiotherapistProvider = ({children}) => {
  const [physiotherapistId, setPhysiotheapistId] = useState(1);
  return (
    <PhysiotherapistContext.Provider value={{ physiotherapistId, setPhysiotheapistId}}>
      {children} {/* This will render the children components */}
    </PhysiotherapistContext.Provider>
  );
};

export const usePhysiotherapist = () => {
  return useContext(PhysiotherapistContext);
}
