// src/pages/LoanPackager.jsx
import React, { useState } from "react";

function LoanPackager() {
  const [packagingStatus, setPackagingStatus] = useState("");

  const handlePackageLoans = () => {
    // Simulate a success response
    setPackagingStatus("Loans successfully packaged!");
  };

  return (
    <div className="loan-packager">
      <h2>Loan Packaging</h2>
      <p>Review and confirm loan packages below:</p>
      
      {/* Placeholder for rate calculation */}
      <h3>Expected Rate: 2.33%</h3>

      <button onClick={handlePackageLoans}>Confirm Packaging</button>

      {packagingStatus && (
        <p className="status-message">{packagingStatus}</p>
      )}
    </div>
  );
}

export default LoanPackager;
