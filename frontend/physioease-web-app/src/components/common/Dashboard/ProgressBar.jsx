import React from 'react';

const ProgressBar = ({ progress, label }) => {
  return (
    <div className="flex items-center justify-between mb-4">
      <div className="w-full bg-gray-200 rounded-full h-4">
        <div className="bg-green-300 h-4 rounded-full" style={{ width: `${progress}%` }} />
      </div>
      <div className="text-gray-500 text-sm ml-4">{label}</div>
    </div>
  );
};

export default ProgressBar;
