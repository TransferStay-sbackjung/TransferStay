import { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

function AuthCallback() {
    const location = useLocation();
    const navigate = useNavigate();

    useEffect(() => {
        const path = location.pathname;
        // path에서 /auth/ 뒤에 있는 토큰 추출
        const token = path.split('/auth/')[1];

        if (token) {
            // 추출한 토큰을 로컬 스토리지에 저장
            localStorage.setItem('token', token);
            console.log("OAuth Token:", token);
            alert('Login successful!');
            // 원하는 경로로 리다이렉트 (메인 페이지 또는 대시보드)
            navigate('/');
        } else {
            console.error('Token not found in URL.');
            alert('Login failed!');
            navigate('/login'); // 로그인 페이지로 리다이렉트
        }
    }, [location, navigate]);

    return (
        <div>Loading...</div>
    );
}

export default AuthCallback;
