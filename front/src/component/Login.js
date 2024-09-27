import React from 'react';
import '../styles/Login.css'; // 스타일 파일

function Login() {
    const handleEmailLogin = () => {
        window.location.href = '/email-login'; // 이메일 로그인 페이지로 리다이렉트
    };

    return (
        <div className="login-container">
            <header className="login-header">
                <h1>로그인하고 서비스 이용하기</h1>
            </header>
            <div className="login-buttons">
                <button onClick={handleEmailLogin}>이메일로 시작하기</button>
                <button onClick={() => window.location.href='http://localhost:8080/oauth2/authorization/kakao'}>카카오로 시작하기</button>
                <button onClick={() => window.location.href='http://localhost:8080/oauth2/authorization/naver'}>네이버로 시작하기</button>
            </div>
        </div>
    );
}

export default Login;
