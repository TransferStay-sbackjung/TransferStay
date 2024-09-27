import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './component/Login'; // Login 컴포넌트를 import
import EmailLogin from "./component/EmailLogin";
import AuthCallback from "./component/AuthCallback";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/login" element={<Login />} />
                <Route path="/email-login" element={<EmailLogin />} />
                <Route path="/auth/:token" element={<AuthCallback />} /> {/* 토큰을 처리하는 경로 */}
            </Routes>
        </Router>
    );
}

export default App;
