// src/SearchResults.js
import React from 'react';
import { useLocation } from 'react-router-dom';

const SearchResults = () => {
  const query = new URLSearchParams(useLocation().search).get('query');

  return (
    <div>
      <h2>검색 결과: {query}</h2>
      {/* 검색 결과 목록을 여기에 표시 */}
    </div>
  );
};

export default SearchResults;
