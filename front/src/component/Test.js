import { useEffect, useState } from "react";
import axios from "axios";
import '../App.css';
import {API_BASE_URL} from "../App"; // 스타일 파일을 import



// test
function App() {
    const [hello, setHello] = useState('');

    useEffect(() => {
        // API 요청 보내기 (JWT 토큰을 Authorization 헤더에 추가)
        axios.get(`${API_BASE_URL}/api/test`)
            .then((res) => {
                setHello(res.data);  // 응답 데이터를 상태에 저장
            })
            .catch((error) => {
                console.error('Error fetching data:', error);  // 에러 처리
            });
    }, []);


    return (
        <div className="App">
            <header className="App-header">
                <h1>백엔드 데이터</h1>
                <p>{hello ? hello : "데이터를 불러오는 중..."}</p> {/* 데이터를 화면에 렌더링 */}
            </header>
        </div>
    );
}

export default App;
