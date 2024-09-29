// src/App.js
import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

// mainPage
import MainPage from './component/MainPage';

// auth
import LoginPage from './component/auth/Login';
import EmailLogin from './component/auth/EmailLogin';
import SignUp from './component/auth/SignUp';

// post
import WritePost from './component/post/WritePost'; // 게시글 작성 페이지 컴포넌트
import AllPosts from './component/post/AllPosts'; // 전체 게시글 조회 컴포넌트
import SearchResults from './component/SearchResults'; // 게시글 검색 결과 컴포넌트

// myPage
import MyPage from './component/MyPage'; // 마이페이지 컴포넌트

// auction
import AuctionPosts from './component/AuctionPosts'; // 전체 경매글 조회 컴포넌트

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/search" element={<SearchResults />} />
        <Route path="/email-login" element={<EmailLogin />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/write-post" element={<WritePost />} />
        <Route path="/my-page" element={<MyPage />} />
        <Route path="/all-posts" element={<AllPosts />} />
        <Route path="/auction-posts" element={<AuctionPosts />} />
      </Routes>
    </Router>
  );
};

export default App;
