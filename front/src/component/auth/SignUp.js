import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../../styles/auth/SignUp.css';
import {API_BASE_URL} from "../../App"; // 스타일 파일

function SignUp() {
    const [email, setEmail] = useState(''); // email로 변경
    const [password, setPassword] = useState('');
    const [passwordCheck, setPasswordCheck] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const navigate = useNavigate(); // 페이지 이동을 위한 useNavigate 사용

    const handleSignUp = async () => {
        if (password !== passwordCheck) {
            alert('비밀번호가 일치하지 않습니다.');
            return;
        }

        try {
            const response = await axios.post(`${API_BASE_URL}/api/v1/user/join`, {
                email, // name 대신 email 사용
                password,
                passwordCheck, // 서버가 받는 passwordCheck도 함께 보내기
                phoneNumber
            });

            console.log(response);
            if (response.status === 200) {
                alert('회원가입이 성공적으로 완료되었습니다!');
                navigate('/login'); // 회원가입 후 로그인 페이지로 이동
            } else {
                alert('회원가입 실패!');
            }
        } catch (error) {
            console.error('회원가입 실패:', error);
            alert('회원가입 실패!');
        }
    };

    return (
        <div className="signup-container">
            <header className="signup-header">
                <h1>필수 정보 입력</h1>
                <div className="form-group">
                    <label>이메일</label>
                    <input
                        type="text"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)} // name 대신 email로 수정
                    />
                </div>
                <div className="form-group">
                    <label>비밀번호</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>
                <div className="form-group">
                    <label>비밀번호 확인</label>
                    <input
                        type="password"
                        value={passwordCheck}
                        onChange={(e) => setPasswordCheck(e.target.value)} // 변수 이름 수정
                    />
                </div>
                <div className="form-group">
                    <label>연락처</label>
                    <input
                        type="text"
                        value={phoneNumber}
                        onChange={(e) => setPhoneNumber(e.target.value)} // 변수 이름 수정
                    />
                </div>
                <button onClick={handleSignUp}>회원가입</button>
            </header>
        </div>
    );
}

export default SignUp;
