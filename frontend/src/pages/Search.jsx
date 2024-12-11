// src/pages/Search.jsx
import React, { useState } from "react";
import FilterManager from "../components/FilterManager";

function Search() {
  const [filters, setFilters] = useState([]);

  return (
    <div className="search-page">
      <h2>Search Mortgages</h2>
      <FilterManager filters={filters} setFilters={setFilters} />
    </div>
  );
}

export default Search;
