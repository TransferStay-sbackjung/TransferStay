import React, { useState } from 'react';
import axios from 'axios';
import '../styles/EmailLogin.css'; // 스타일 파일

function EmailLogin() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = async () => {
        try {
            const response = await axios.post('http://localhost:8080/email-login', { email, password });

            // 응답 헤더에서 Authorization 토큰을 추출
            const token = response.headers['authorization']?.split(' ')[1]; // 'Bearer [token]'에서 토큰 부분만 추출
            if (token) {
                localStorage.setItem('token', token);
                console.log("email-Login Token:", token);
                alert('Login successful!');
            } else {
                console.error('Token not found in response.');
                alert('Login failed!');
            }
        } catch (error) {
            console.error('Login failed:', error);
            alert('Login failed!');
        }
    };

    return (
        <div className="email-login-container">
            <header className="email-login-header">
                <h1>이메일로 로그인</h1>
                <div className="form-group">
                    <label>Email</label>
                    <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
                </div>
                <div className="form-group">
                    <label>Password</label>
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
                </div>
                <button onClick={handleSubmit}>Sign In</button>
                <button className="link-button" onClick={() => console.log('Forgot password?')}>
                    Forgot password?
                </button>
            </header>
        </div>
    );
}

export default EmailLogin;
