// src/pages/Search.jsx
import React, { useState } from "react";
import FilterManager from "../components/FilterManager";
import FilterSummary from "../components/FilterSummary";
import MortgageResultsTable from "../components/MortgageResultsTable";

function Search() {
  const [filters, setFilters] = useState([]);
  const [results, setResults] = useState([]);

  const handleDeleteFilter = (index) => {
    setFilters(filters.filter((_, i) => i !== index));
  };

  return (
    <div className="search-page">
      <h2>Search Mortgages</h2>
      <FilterManager filters={filters} setFilters={setFilters} />
      <FilterSummary filters={filters} handleDelete={handleDeleteFilter} />
      <MortgageResultsTable results={results} />
    </div>
  );
}

export default Search;
