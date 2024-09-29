import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/NavBar.css'; // 스타일 파일

const NavBar = () => {
    const navigate = useNavigate();
    const [isLoggedIn, setIsLoggedIn] = useState(false); // 로그인 상태 관리

    // 컴포넌트가 마운트될 때 localStorage에 토큰이 있는지 확인
    useEffect(() => {
        const checkLoginStatus = () => {
            const token = localStorage.getItem('token'); // localStorage에서 토큰 확인
            setIsLoggedIn(!!token); // 토큰이 있으면 true, 없으면 false
        };

        checkLoginStatus(); // 컴포넌트 마운트 시 로그인 상태 확인

        // 로그인/로그아웃 후 토큰이 변경될 때마다 상태를 확인
        window.addEventListener('storage', checkLoginStatus);

        // 컴포넌트가 언마운트될 때 이벤트 리스너 제거
        return () => {
            window.removeEventListener('storage', checkLoginStatus);
        };
    }, []);

    // 로그아웃 처리
    const handleLogout = () => {
        localStorage.removeItem('token'); // localStorage에서 토큰 삭제
        setIsLoggedIn(false); // 로그인 상태를 false로 변경
        navigate('/login'); // 로그인 페이지로 리다이렉트
    };

    return (
        <nav className="navbar">
            <div className="navbar-logo" onClick={() => navigate('/')}>
                TransferStay
            </div>
            <div className="navbar-links">
                <span onClick={() => navigate('/write-post')}>게시글 작성하기</span>
                <span onClick={() => navigate('/all-posts')}>전체 게시글 조회</span>
                <span onClick={() => navigate('/auction-posts')}>전체 경매글 조회</span>
                <span onClick={() => navigate('/my-page')}>마이페이지</span>

                {/* 로그인 상태에 따라 로그인 또는 로그아웃 버튼 표시 */}
                {!isLoggedIn ? (
                    <span onClick={() => navigate('/login')} className="navbar-login">로그인</span>
                ) : (
                    <span onClick={handleLogout} className="navbar-login">로그아웃</span>
                )}
            </div>
        </nav>
    );
};

export default NavBar;
