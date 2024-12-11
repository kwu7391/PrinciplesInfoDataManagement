// src/App.jsx
import React from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import Dashboard from "./pages/Dashboard";
import Search from "./pages/Search";
import LoanPackager from "./components/LoanPackager";

function App() {
  return (
    <Router>
      <div className="app-container">
        <header>
          <h1>Mortgage Backed Securities System</h1>
          <nav>
            <Link to="/dashboard">Dashboard</Link>
            <Link to="/search">Search Mortgages</Link>
            <Link to="/package">Loan Packaging</Link>
          </nav>
        </header>

        <main>
          <Routes>
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/search" element={<Search />} />
            <Route path="/package" element={<LoanPackager />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
