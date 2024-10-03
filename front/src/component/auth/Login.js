import React from 'react';
import { useNavigate } from 'react-router-dom'; // 페이지 이동을 위한 useNavigate 사용
import '../../styles/auth/Login.css';
import {API_BASE_URL} from "../../App"; // 스타일 파일

function Login() {
    const navigate = useNavigate(); // useNavigate 훅 사용

    // 회원가입 페이지로 이동
        const handleSignUp = () => {
            navigate('/signup'); // 회원가입 페이지로 리다이렉트
        };

    // 이메일 로그인 페이지로 이동
    const handleEmailLogin = () => {
        navigate('/email-login'); // 이메일 로그인 페이지로 리다이렉트
    };

    // 카카오 로그인 처리
    const handleKakaoLogin = () => {
        window.location.href = `${API_BASE_URL}/oauth2/authorization/kakao`; // 카카오 OAuth URL
    };

    // 네이버 로그인 처리
    const handleNaverLogin = () => {
        window.location.href = `${API_BASE_URL}/oauth2/authorization/naver`; // 네이버 OAuth URL
    };

    return (
        <div className="login-container">
            <header className="login-header">
                <h1>로그인하고 서비스 이용하기</h1>
            </header>
            <div className="login-buttons">
                <button className="signup-button" onClick={handleSignUp}>회원가입하기</button>
                <button className="login-button" onClick={handleEmailLogin}>이메일로 시작하기</button>
                <button className="login-button" onClick={handleKakaoLogin}>카카오로 시작하기</button>
                <button className="login-button" onClick={handleNaverLogin}>네이버로 시작하기</button>
            </div>
        </div>
    );
}
export default Login;
