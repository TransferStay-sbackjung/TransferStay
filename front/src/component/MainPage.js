import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/MainPage.css'; // 스타일 파일
import NavBar from './NavBar';  // 네비게이션 바 컴포넌트 임포트

const MainPage = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const navigate = useNavigate();

  const handleSearch = (e) => {
    e.preventDefault();
    console.log(`Searching for: ${searchQuery}`);
  };

  return (
    <div className="main-page">
      <NavBar />  {/* 네비게이션 바 추가 */}

      <section className="search-section">
        <h2>원하는 숙소를 검색해보세요</h2>
        <form onSubmit={handleSearch} className="search-form">
          <input
            type="text"
            placeholder="지역을 입력해 주세요"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="search-input"
          />
          <div className="dropdowns">
            <input type="text" placeholder="체크인 날짜" className="date-input" />
            <input type="text" placeholder="체크아웃 날짜" className="date-input" />
            <input type="text" placeholder="인원" className="guest-input" />
          </div>
          <button type="submit" className="search-button">Search</button>
        </form>
      </section>

      <section className="recent-listings">
        <h3>최근 올라온 양도글</h3>
        <div className="listings-grid">
          {/* 임시 양도글 카드 */}
          {[...Array(8)].map((_, i) => (
            <div className="listing-card" key={i}>
              <div className="listing-image">Image</div>
              <div className="listing-info">
                <h4>호텔 이름</h4>
                <p>지역 이름</p>
                <p>가격 per night</p>
              </div>
            </div>
          ))}
        </div>
      </section>

      <footer>
        <div className="footer-links">
          <button className="link-button" onClick={() => window.location.href='#'}>Contact Us</button>
          <button className="link-button" onClick={() => window.location.href='#'}>About StaySwap</button>
          <button className="link-button" onClick={() => window.location.href='#'}>Terms of Service</button>
        </div>
      </footer>
    </div>
  );
};

export default MainPage;
