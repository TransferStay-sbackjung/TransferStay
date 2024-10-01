import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import axios, {get} from 'axios'; // API 요청을 위해 axios 임포트
import NavBar from './NavBar';  // 네비게이션 바 컴포넌트 임포트
import '../styles/post/SearchPost.css';
import {API_BASE_URL} from "../App"; // 스타일 임포트

const SearchResults = () => {
    const [posts, setPosts] = useState([]);
    const [noResults, setNoResults] = useState(false);
    const [headerMessage, setHeaderMessage] = useState('');  // 검색 조건을 보여줄 메시지 상태
    const location = useLocation();

    // 쿼리 파라미터에서 검색 조건 가져오기
    useEffect(() => {
        const queryParams = new URLSearchParams(location.search);
        const freeField = queryParams.get('freeField');
        const checkInDate = queryParams.get('checkInDate');
        const checkOutDate = queryParams.get('checkOutDate');
        const personnel = queryParams.get('personnel');
        const locationDepth1 = queryParams.get('locationDepth1');
        const locationDepth2 = queryParams.get('locationDepth2');

        // 메시지 설정: 조건에 따라 표시
        let message = '';
        if (locationDepth1) message += `${locationDepth1} | `;
        if (locationDepth2) message += `${locationDepth2} | `;
        if (checkInDate) message += `${checkInDate} 체크인 | `;
        if (checkOutDate) message += `${checkOutDate} 체크아웃 | `;
        if (checkOutDate) message += `${checkOutDate} 체크아웃 | `;
        if (personnel) message += `${personnel} 명 | `;
        if (freeField) message += `${freeField}  `;

        setHeaderMessage(message);
    }, [location.search]);

    // GET 요청을 사용하여 검색 결과 데이터를 받아오는 함수
    useEffect(() => {
        const fetchPosts = async () => {
            try {
                const searchParams = new URLSearchParams(location.search);
                const queryString = searchParams.toString();
                const response = await axios.get(`${API_BASE_URL}/api/v1/search?${queryString}`);

                if (response.status === 200) {
                    setPosts(response.data.data); // 검색 결과가 있을 경우 데이터 설정
                } else if (response.status === 204) {
                    setNoResults(true); // 201 코드일 경우 검색 결과가 없음
                } else {
                    throw new Error('검색 실패');
                }
            } catch (error) {
                console.error('데이터를 가져오는 중 오류 발생:', error);
            }
        };

        fetchPosts();
    }, [location.search]);

    return (
        <div className="search-posts-container">
            <NavBar/>
            <div id="wrap" className="search-results-page">
                <h1 className="search-results-title">
                    <span className="search-data">{headerMessage}</span>
                    <span>의 검색결과 입니다</span>
                </h1>
                {noResults ? (
                    <p className="no-results">양도글이 존재하지 않습니다</p>
                ) : (
                    <div className="search-results-grid">
                        {posts.map((post) => (
                            <div key={post.id} className="search-result-card">
                                <div className="card-image">Image</div>
                                <div className="card-info">
                                    <h4>{post.title}</h4>
                                    <p>{post.price.toLocaleString()}원</p>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
            <footer>
                <div className="footer-links">
                    <button className="link-button" onClick={() => window.location.href = '#'}>Contact Us</button>
                    <button className="link-button" onClick={() => window.location.href = '#'}>About StaySwap</button>
                    <button className="link-button" onClick={() => window.location.href = '#'}>Terms of Service</button>
                </div>
            </footer>
        </div>
    );
};

export default SearchResults;