import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/MainPage.css'; // 스타일 파일
import NavBar from './NavBar';
import {API_BASE_URL} from "../App";  // 네비게이션 바 컴포넌트 임포트

const MainPage = () => {
    const [searchQuery, setSearchQuery] = useState('');
    const [locationDepth1, setLocationDepth1] = useState('');
    const [locationDepth2, setLocationDepth2] = useState('');
    const [checkInDate, setCheckInDate] = useState('');
    const [checkOutDate, setCheckOutDate] = useState('');
    const [person, setPerson] = useState('');
    const [listings, setListings] = useState([]); // 양도글 목록을 저장할 상태
    const [loading, setLoading] = useState(true); // 로딩 상태

    const navigate = useNavigate();

    // 검색 함수
    const handleSearch = (e) => {
        e.preventDefault();

        const queryParams = new URLSearchParams();

        if (searchQuery) queryParams.append('freeField', searchQuery);
        if (locationDepth1) queryParams.append('locationDepth1', locationDepth1);
        if (locationDepth2) queryParams.append('locationDepth2', locationDepth2);
        if (checkInDate) queryParams.append('checkInDate', checkInDate);
        if (checkOutDate) queryParams.append('checkOutDate', checkOutDate);
        if (person) queryParams.append('person', person);

        navigate(`/search?${queryParams.toString()}`);
    };

    // 데이터 받아오기
    useEffect(() => {
        const fetchListings = async () => {
            try {
                const response = await fetch(`${API_BASE_URL}/api/v1/assignment-posts`); // 적절한 엔드포인트로 변경
                const result = await response.json();
                setListings(result.data.content); // 받아온 데이터를 상태로 설정
                setLoading(false); // 로딩 완료
            } catch (error) {
                console.error("Error fetching listings:", error);
                setLoading(false); // 오류 발생 시 로딩 중단
            }
        };

        fetchListings();
    }, []);

    if (loading) {
        return <div>로딩 중...</div>; // 로딩 중일 때 표시
    }

    // 6개를 채우기 위한 임시 카드 생성
    const emptyCards = Array.from({ length: Math.max(6 - listings.length, 0) });

    return (
        <div className="main-page">
            <NavBar />  {/* 네비게이션 바 추가 */}

            {/* 검색 섹션 */}
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

            {/* 최근 올라온 양도글 섹션 */}
            <section className="recent-listings">
                <h3>최근 올라온 양도글</h3>
                <div className="listings-grid">
                    {listings.length > 0 ? (
                        listings.map((listing) => (
                            <div
                                className="listing-card"
                                key={listing.id}
                                onClick={() => navigate(`/posts/${listing.id}`)} // 게시글 클릭 시 상세 페이지로 이동
                            >
                                <div className="listing-image">Image</div> {/* 이미지 URL 추가 가능 */}
                                <div className="listing-info">
                                    <h4>{listing.title}</h4>
                                    <p>{listing.locationDepth1} {listing.locationDepth2}</p>
                                    <p>{listing.price.toLocaleString()} 원</p>
                                    <p>체크인: {listing.checkInDate}</p>
                                    <p>체크아웃: {listing.checkOutDate}</p>
                                    <p>상태: {listing.status}</p>
                                </div>
                            </div>
                        ))
                    ) : (
                        <p>최근 올라온 양도글이 없습니다.</p>
                    )}
                    {/* 빈 카드 채우기 */}
                    {emptyCards.map((_, index) => (
                        <div className="listing-card empty" key={`empty-${index}`}>
                            <div className="listing-image">No Image</div>
                            <div className="listing-info">
                                <h4>빈 게시글 자리</h4>
                                <p>정보 없음</p>
                            </div>
                        </div>
                    ))}
                </div>
            </section>
        </div>
    );
};

export default MainPage;
