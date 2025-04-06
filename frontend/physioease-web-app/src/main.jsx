import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import { PatientProvider } from './context/PatientContext.jsx'
import { PhysiotherapistProvider } from './context/PhysiotherapistContext'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <PatientProvider>
      <PhysiotherapistProvider>
        <App />
      </PhysiotherapistProvider>
    </PatientProvider>
  </StrictMode>,
)
