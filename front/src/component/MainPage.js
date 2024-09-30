import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/MainPage.css'; // 스타일 파일
import NavBar from './NavBar';  // 네비게이션 바 컴포넌트 임포트

const MainPage = () => {
    const [searchQuery, setSearchQuery] = useState('');
    const [locationDepth1, setLocationDepth1] = useState('');
    const [locationDepth2, setLocationDepth2] = useState('');
    const [checkInDate, setCheckInDate] = useState('');
    const [checkOutDate, setCheckOutDate] = useState('');
    const [person, setPerson] = useState('');

    const navigate = useNavigate();

    // 검색 함수
    const handleSearch = (e) => {
        e.preventDefault();

        // 조건이 하나도 없을 때 alert 표시
        if (!searchQuery && !locationDepth1 && !locationDepth2 && !checkInDate && !checkOutDate && !person) {
            alert('검색할 조건을 작성해주세요');
            return;
        }

        const queryParams = new URLSearchParams();

        if (searchQuery) queryParams.append('freeField', searchQuery);
        if (locationDepth1) queryParams.append('locationDepth1', locationDepth1);
        if (locationDepth2) queryParams.append('locationDepth2', locationDepth2);
        if (checkInDate) queryParams.append('checkInDate', checkInDate);
        if (checkOutDate) queryParams.append('checkOutDate', checkOutDate);
        if (person) queryParams.append('person', person);

        navigate(`/search?${queryParams.toString()}`);
    };

    return (
        <div className="main-page">
            <NavBar />  {/* 네비게이션 바 추가 */}

            <section className="search-section">
                <h2>원하는 숙소를 검색해보세요</h2>
                <form onSubmit={handleSearch} className="search-form">
                    <input
                        type="text"
                        placeholder="자유 검색어"
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                        className="search-input"
                    />
                    <div className="dropdowns">
                        <input
                            type="text"
                            placeholder="지역(큰 범위)"
                            value={locationDepth1}
                            onChange={(e) => setLocationDepth1(e.target.value)}
                            className="location-input"
                        />
                        <input
                            type="text"
                            placeholder="지역(작은 범위)"
                            value={locationDepth2}
                            onChange={(e) => setLocationDepth2(e.target.value)}
                            className="location-input"
                        />
                        <input
                            type="date"
                            placeholder="체크인 날짜"
                            value={checkInDate}
                            onChange={(e) => setCheckInDate(e.target.value)}
                            className="date-input"
                        />
                        <input
                            type="date"
                            placeholder="체크아웃 날짜"
                            value={checkOutDate}
                            onChange={(e) => setCheckOutDate(e.target.value)}
                            className="date-input"
                        />
                        <input
                            type="number"
                            placeholder="인원"
                            value={person}
                            onChange={(e) => setPerson(e.target.value)}
                            className="guest-input"
                        />
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
